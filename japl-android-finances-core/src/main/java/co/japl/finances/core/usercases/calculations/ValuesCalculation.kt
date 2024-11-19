package co.japl.finances.core.usercases.calculations

import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class ValuesCalculation @Inject constructor(){

    internal fun getCapital(codeBought:Int,valueItem:Double,month: Short,differQuotes:List<DifferInstallmentDTO?>?): Double {
        return month.takeIf { it > 1}?.let{
            differQuotes?.firstOrNull { it?.cdBoughtCreditCard?.toInt() == codeBought }
                ?.let {
                    it.pendingValuePayable / it.newInstallment
                } ?: (valueItem / month)
        }?: valueItem

    }

    internal fun getQuotesPaid(codeBought:Int,month:Short,boughtDate:LocalDateTime, cutOff: LocalDateTime, differQuotes: List<DifferInstallmentDTO?>?):Short{
        return differQuotes?.firstOrNull{it?.cdBoughtCreditCard?.toInt() == codeBought}?.let{
            DateUtils.getMonths(LocalDateTime.of(it.create, LocalTime.MAX),cutOff).toShort()
        } ?: month.takeIf {it > 1}?.let {
            (DateUtils.getMonths(boughtDate,cutOff) + 1).toShort()
        }?:1.toShort()
    }

    internal fun getPendingToPay(codeBought:Int,month:Short,monthPaid:Short,valueItem:Double,capitalValue:Double, differQuotes:List<DifferInstallmentDTO?>?):Double{
        return month.takeIf {  it > 1}?.let{
            val differQuote = differQuotes?.firstOrNull{ it?.cdBoughtCreditCard?.toInt() == codeBought }
            val capitalBought = capitalValue * (monthPaid - 1)
            return (differQuote?.let {it.pendingValuePayable - capitalBought
            }?:valueItem.minus(capitalBought))
                .takeIf { it > 0.0 }?: 0.0
        }?: valueItem
    }
}