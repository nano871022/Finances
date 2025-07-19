package co.japl.finances.core.usercases.calculations

import android.util.Log
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import kotlin.math.pow

class ValuesCalculation @Inject constructor(){

    internal fun getCapital(codeBought:Int,valueItem:Double,month: Short,differQuotes:List<DifferInstallmentDTO?>?): Double {
        return (month.takeIf { it > 1}?.let{
            differQuotes?.firstOrNull { it?.cdBoughtCreditCard?.toInt() == codeBought }
                ?.let {
                    it.pendingValuePayable / it.newInstallment
                } ?: (valueItem / month)
        }?: valueItem).also{
            Log.d("getcapitalquote","$codeBought $valueItem $month $differQuotes $it")
        }

    }

    internal fun getQuotesPaid(codeBought:Int,month:Short,boughtDate:LocalDateTime, cutOff: LocalDateTime, differQuotes: List<DifferInstallmentDTO?>?):Short{
        return (differQuotes?.firstOrNull{it?.cdBoughtCreditCard?.toInt() == codeBought}?.let{
            DateUtils.getMonths(LocalDateTime.of(it.create, LocalTime.MAX),cutOff).toShort()
        } ?: month.takeIf {it > 1}?.let {
            (DateUtils.getMonths(boughtDate,cutOff) + 1).toShort()
        }?:1.toShort()).also{
            Log.d("quotespaid","$codeBought $month $boughtDate $cutOff $differQuotes $it")
        }
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

    internal fun calculateQuoteCredit(value:Double,rateEM:Double,months:Short):Double{
        val interest = rateEM
        val step1 = 1 + interest
        val step1PowPeriods = step1.pow(months.toDouble())
        val step2 =   interest * step1PowPeriods
        val step3 = step1PowPeriods - 1
        return ((value) * (step2 / step3))
    }

    internal fun calculatePendingCapital(taxNM:Double,totalPeriods:Short,quoteValue:Double,monthPaid:Short):Double{
        val interest = (taxNM)
        val firstStep = 1 + interest

        val secondStep = firstStep.pow(-(totalPeriods.toDouble() - monthPaid))
        val thirdStep = 1 - secondStep

        val fourthStep = thirdStep / interest
        return (quoteValue * fourthStep).also {
            Log.d(javaClass.simpleName," $quoteValue $it")

        }
    }
}