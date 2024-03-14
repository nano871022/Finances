package co.japl.finances.core.usercases.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapCurrentMonthly
import co.com.japl.finances.iports.dtos.RecapLastMonthly
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.InterestRateCalculation
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.interfaces.creditcard.ITax
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.mapper.CreditCardMap
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import javax.inject.Inject

class Bought @Inject constructor(private val boughtSvc:IQuoteCreditCardPort,private val service:IBoughtList,private val creditRateSvc:ITax, private val interestCalculation:InterestCalculations, private val valuesCalculation:ValuesCalculation):IBought{

    override fun getRecap(creditCard:CreditCardDTO,cutOffDate:LocalDateTime,cache:Boolean):RecapMonthly?{
        return RecapMonthly(getCurrentRecap(
            CreditCardMap.mapper(creditCard),
            cutOffDate,cache
        ),getLastRecap(
            CreditCardMap.mapper(creditCard),
            cutOffDate,cache
        ),getGraphList(
            CreditCardMap.mapper(creditCard),
            cutOffDate,cache
        ))
    }

    override fun getBoughtCurrentPeriodList(
        creditCardDTO: CreditCardDTO,
        cutOff: LocalDateTime,
        cache: Boolean
    ): List<Pair<String, Double>>? {
        val creditCard = CreditCardMap.mapper(creditCardDTO)
        return service.getBoughtList(creditCard,cutOff,cache)?.let{
            val itlist = it.list?.filter{
                it.boughtDate in DateUtils.startDateFromCutoff(creditCard.cutoffDay,cutOff)..cutOff
            }
            val list = arrayListOf<Pair<String,Double>>()
            itlist?.filter {
                it.tagName.isNotBlank() && it.recurrent.not() }?.map { Pair(it.tagName,it.quoteValue)
                }?.groupBy { it.first }?.map{
                Pair(it.key,it.value.sumOf { it.second })
            }?.let(list::addAll)

            itlist?.filter {
                it.tagName.isBlank() && it.recurrent.not() && it.month == 1
            }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair("1 Cuota",it)) }

            itlist?.filter {
                it.tagName.isBlank()  && it.recurrent.not() && it.month > 1
            }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair(">1 Cuota",it)) }

            itlist?.filter {
                it.tagName.isBlank() && it.recurrent && it.month == 1 }?.sumOf{it.quoteValue
                }?.takeIf { it > 0 }?.let { list.add(Pair("Recurrente 1 Cuota",it)) }

            itlist?.filter {
                it.tagName.isBlank()  && it.recurrent && it.month > 1
            }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair("Recurrente >1 Cuota",it)) }
            list
        }
    }

    override fun create(creditCardBoughtDTO: CreditCardBoughtDTO,cache: Boolean): Int {
        return boughtSvc.create(creditCardBoughtDTO,cache)
    }

    override fun update(creditCardBoughtDTO: CreditCardBoughtDTO,cache: Boolean): Boolean {
        return boughtSvc.update(creditCardBoughtDTO,cache)
    }

    override fun quoteValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double {
        val capital =  capitalValue(
            codeCreditRate,
            months,
            value,
            kindOfTax,
            kindOfInterest
        )
        val interest = interestValue(
            codeCreditRate,
            months,
            value,
            kindOfTax,
            kindOfInterest
        )
        return capital +  interest
    }

    override fun capitalValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double {
        return valuesCalculation.getCapital(0,value,months, emptyList())
    }

    override fun interestValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum

    ): Double {
        creditRateSvc.getById(codeCreditRate)?.let {it->
            val rate = InterestRateCalculation.getNM(it.value, it.kindOfTax!!)
            return interestCalculation.getInterestValue(
                    months, 0, it.kind, value, value, rate, KindOfTaxEnum.MONTLY_NOMINAL, false, false

                )
            }
        return 0.0
    }

    override fun getById(codeBought: Int,cache: Boolean): CreditCardBoughtDTO? {
        return boughtSvc.get(codeBought,cache)
    }

    private fun getCurrentRecap(creditCard:CreditCard,cutOffDate:LocalDateTime,cache: Boolean):RecapCurrentMonthly?{
        return service.getBoughtList(creditCard,cutOffDate,cache)?.let{
            val capitalValue = it.list?.sumOf { it.capitalValue }?:0.0
            val interestValue = it.list?.sumOf { it.interestValue }?:0.0
            val quoteValue = it.list?.sumOf { it.quoteValue }?:0.0
            val numQuotes = it.list?.count { it.month > 1 }?:0
            val numOneQuote = it.list?.count { it.month == 1 }?:0
            RecapCurrentMonthly(quoteValue,numQuotes,numOneQuote,capitalValue,interestValue)
        }
    }

    private fun getLastRecap(creditCard:CreditCard,cutOffDate:LocalDateTime,cache: Boolean):RecapLastMonthly?{
        val lastCutOff = DateUtils.cutOffLastMonth(creditCard.cutoffDay,cutOffDate)
        return service.getBoughtList(creditCard,lastCutOff,cache)?.let{
            val totalQuote = it.list?.sumOf { it.quoteValue }?:0.0
            val capitalOneQuote = it.list?.filter { it.month == 1 }?.sumOf { it.capitalValue }?:0.0
            val capitalQuotes = it.list?.filter { it.month > 1 }?.sumOf { it.capitalValue }?:0.0
            val interestValue = it.list?.sumOf { it.interestValue }?:0.0
            RecapLastMonthly(totalQuote,capitalOneQuote,capitalQuotes,interestValue)
        }
    }

    private fun getGraphList(creditCard:CreditCard,cutOffDate:LocalDateTime,cache: Boolean):List<Pair<String,Double>>?{
        return service.getBoughtList(creditCard,cutOffDate,cache)?.let{
            val list = arrayListOf<Pair<String,Double>>()
            it.list?.filter { it.tagName.isNotBlank() && it.recurrent.not() }?.map { Pair(it.tagName,it.quoteValue) }?.groupBy { it.first }?.map{
                Pair(it.key,it.value.sumOf { it.second })
            }?.let(list::addAll)
            it.list?.filter { it.tagName.isBlank() && it.recurrent.not() && it.month == 1 }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair("1 Cuota",it)) }
            it.list?.filter { it.tagName.isBlank()  && it.recurrent.not() && it.month > 1 }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair(">1 Cuota",it)) }
            it.list?.filter { it.tagName.isBlank() && it.recurrent && it.month == 1 }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair("Recurrente 1 Cuota",it)) }
            it.list?.filter { it.tagName.isBlank()  && it.recurrent && it.month > 1 }?.sumOf{it.quoteValue}?.takeIf { it > 0 }?.let { list.add(Pair("Recurrente >1 Cuota",it)) }
            list
        }
    }
}