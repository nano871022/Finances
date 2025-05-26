package co.japl.finances.core.usercases.implement.common

import android.util.Log
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.model.Tax
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.mapper.CreditCardBoughtItemMapper
import co.japl.finances.core.utils.DateUtils
import org.intellij.lang.annotations.RegExp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class ListBoughts @Inject constructor(
    private val quoteCCSvc: IQuoteCreditCardPort
    ,private val valuesCalculation: ValuesCalculation
    , private val interestCalculation: InterestCalculations
){

    internal fun getList(creditCard: CreditCard,cutOff:LocalDateTime,cache:Boolean): List<CreditCardBoughtItemDTO> {
        val startDate = DateUtils.startDateFromCutoff(creditCard.cutoffDay,cutOff)
        val mainList = getMainList(creditCard,startDate,cutOff,cache)
        val recurrentList = getRecurrentList(creditCard,cutOff)
        val recurrentPendingList = getRecurrentListPending(creditCard,cutOff,cache)
        val pendingList = getPendingList(creditCard,startDate,cutOff,cache)

        val list = ArrayList<CreditCardBoughtDTO>()
        list.addAll(mainList)
        list.addAll(recurrentList)
        list.addAll(recurrentPendingList)
        list.addAll(pendingList)

        return list.map (CreditCardBoughtItemMapper::mapper)
    }


    internal fun calculateValues(dto:CreditCardBoughtItemDTO,creditCard: CreditCard,taxQuote:Tax?,taxAdvance:Tax?,cutoff:LocalDateTime,differQuotes:List<DifferInstallmentDTO?>?):CreditCardBoughtItemDTO{
        val setting = interestCalculation.getSettings(dto.id,dto.interest)
        setting?.let{
            dto.settingCode = it.first
            dto.settings = it.second
        }
        interestCalculation.calculateInterestMonthlyEffective(dto.kind,
            Tax(dto.interest,dto.kindOfTax),taxQuote,taxAdvance,setting).let {
            dto.interest = it.value
            dto.kindOfTax = it.kind
        }

        valuesCalculation.getQuotesPaid(dto.id,dto.month.toShort(),dto.boughtDate,cutoff,differQuotes)?.let { dto.monthPaid = it.toLong()}

        var month = dto.month
        var monthPaid = dto.monthPaid
        var monthDifer:Long? = null
        var originDate:LocalDateTime? = null

        val differ = dto.nameItem.contains("\\([0-9]+\\. [0-9]+\\.[0-9]+\\)".toRegex())
        differ.takeIf { it }?.let {
            var boughtDateDiffer:LocalDateTime? = null
            quoteCCSvc.get(dto.id,false)?.let{
                month = it.month
                boughtDateDiffer = it.boughtDate
                DateUtils.getMonths(it.boughtDate,cutoff)?.let{
                    monthPaid = it + 1
                }
            }
            "\\(([0-9]+)\\. [0-9]+\\.[0-9]+\\)".toRegex().find(dto.nameItem)?.let{
                quoteCCSvc.get(it.groupValues[1].toInt(),false)?.let{
                    monthDifer = DateUtils.getMonths(it.boughtDate,boughtDateDiffer!!)
                    originDate = it.boughtDate
                    DateUtils.getMonths(it.boughtDate,cutoff)?.let{
                        if ( it == "1".toLong() ) {
                            monthPaid = it
                        }
                    }
                }
            }
        }

        interestCalculation.lastInterestCalc(dto,creditCard,differ)?.let {
            dto.interest = it.value
            dto.kindOfTax = it.kind
        }

        valuesCalculation.getCapital(dto.id,dto.valueItem,month.toShort(),differQuotes).let { dto.capitalValue = it}

        valuesCalculation.getPendingToPay(dto.id,month.toShort(),monthPaid.toShort(),dto.valueItem,dto.capitalValue,differQuotes).let { dto.pendingToPay = it}

        interestCalculation.getInterestValue(month.toShort(),
            monthPaid.toShort(),
            dto.kind,
            dto.pendingToPay,
            dto.valueItem,
            dto.interest,
            dto.kindOfTax,
            creditCard.interest1Quote,
            creditCard.interest1NotQuote,
            differ).let { dto.interestValue = it}

        dto.quoteValue = (dto.interestValue?:0.0) + (dto.capitalValue?:0.0)

        dto.month += monthDifer?.toInt()?:0
        dto.monthPaid += monthDifer?.toInt()?:0
        originDate?.let{dto.boughtDate = it}

        Log.w(javaClass.name,"=== getBoughtList id: ${dto.id} toPay: ${dto.pendingToPay} Tax: ${dto.interest} ${dto.kindOfTax} interest: ${dto.interestValue}")
        return dto
    }

    private fun getPendingList(creditCard:CreditCard,startDate: LocalDateTime,cutOff:LocalDateTime,cache: Boolean): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getPendingQuotes(creditCard.codeCreditCard,startDate,cutOff,cache)
    }

    private fun getRecurrentListPending(creditCard: CreditCard,cutOff:LocalDateTime,cache: Boolean): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getRecurrentPendingQuotes(creditCard.codeCreditCard,cutOff,cache).also{
            Log.d(javaClass.name,"=== PendingRecurrentList: ${it.size} $it")
        }
    }

    private fun getRecurrentList(creditCard:CreditCard,cutOff: LocalDateTime): List<CreditCardBoughtDTO> {
        val list =  quoteCCSvc.getRecurrentBuys(creditCard.codeCreditCard,cutOff)
        list.forEach {
            it.boughtDate = it.boughtDate
                .withYear(cutOff.year)
                .withMonth(cutOff.monthValue)
        }
        return list.also{
            Log.d(javaClass.name,"=== RecurrentList: ${it.size} $it")
        }
    }

    private fun getMainList(creditCard:CreditCard,startDate: LocalDateTime,cutOff:LocalDateTime,cache:Boolean): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getToDate(creditCard.codeCreditCard,startDate,cutOff,cache).also {
            Log.d(javaClass.name,"=== MainList: ${it.size} $it")
        }
    }
}