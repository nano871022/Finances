package co.japl.finances.core.usercases.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapCurrentMonthly
import co.com.japl.finances.iports.dtos.RecapLastMonthly
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.mapper.CreditCardMap
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import javax.inject.Inject

class Bought @Inject constructor(private val service:IBoughtList):IBought{

    override fun getRecap(creditCard:CreditCardDTO,cutOffDate:LocalDateTime):RecapMonthly?{
        return RecapMonthly(getCurrentRecap(
            CreditCardMap.mapper(creditCard),
            cutOffDate
        ),getLastRecap(
            CreditCardMap.mapper(creditCard),
            cutOffDate
        ),getGraphList(
            CreditCardMap.mapper(creditCard),
            cutOffDate
        ))
    }
    private fun getCurrentRecap(creditCard:CreditCard,cutOffDate:LocalDateTime):RecapCurrentMonthly?{
        return service.getBoughtList(creditCard,cutOffDate)?.let{
            val capitalValue = it.list?.sumOf { it.capitalValue }?:0.0
            val interestValue = it.list?.sumOf { it.interestValue }?:0.0
            val quoteValue = it.list?.sumOf { it.quoteValue }?:0.0
            val numQuotes = it.list?.count { it.month > 1 }?:0
            val numOneQuote = it.list?.count { it.month == 1 }?:0
            RecapCurrentMonthly(quoteValue,numQuotes,numOneQuote,capitalValue,interestValue)
        }
    }

    private fun getLastRecap(creditCard:CreditCard,cutOffDate:LocalDateTime):RecapLastMonthly?{
        val lastCutOff = DateUtils.cutOffLastMonth(creditCard.cutoffDay,cutOffDate)
        return service.getBoughtList(creditCard,lastCutOff)?.let{
            val totalQuote = it.list?.sumOf { it.quoteValue }?:0.0
            val capitalOneQuote = it.list?.filter { it.month == 1 }?.sumOf { it.capitalValue }?:0.0
            val capitalQuotes = it.list?.filter { it.month > 1 }?.sumOf { it.capitalValue }?:0.0
            val interestValue = it.list?.sumOf { it.interestValue }?:0.0
            RecapLastMonthly(totalQuote,capitalOneQuote,capitalQuotes,interestValue)
        }
    }

    private fun getGraphList(creditCard:CreditCard,cutOffDate:LocalDateTime):List<Pair<String,Double>>?{
        return service.getBoughtList(creditCard,cutOffDate)?.let{
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