package co.japl.android.myapplication.bussiness.impl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class QuoteCredit : Calc {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()

    override fun calc(value: BigDecimal, period: Long, tax: Double,kindOf: KindOfTaxEnum): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val taxValue = kindOfTaxSvc.getNM(tax,kindOf)
        val taxDouble = getTax(taxValue)
        val onePlusTaxPowPeriod = onePlusTaxPowPeriod(taxDouble,period)
        val taxAndPeriodDiv = (taxDouble*onePlusTaxPowPeriod).div(onePlusTaxPowPeriod-1)
        val response = value.multiply(taxAndPeriodDiv.toBigDecimal())
        return response.also { Log.d(this.javaClass.name,"<<<=== FINISH:: Calc: $response = $value X (($taxDouble X $onePlusTaxPowPeriod) / ($onePlusTaxPowPeriod - 1))") }
    }

    private fun onePlusTaxPowPeriod(tax:Double,period:Long):Double{
        val response = (1+tax).pow(period.toDouble())
        return response.also { Log.d(this.javaClass.name,"<<<=== FINISH:: OnePlusTaxPowPeriod: $response = 1 + $tax ^ $period") }
    }
    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        return interest.also { Log.d(this.javaClass.name,"<<<=== FINISH:: getTax: $interest = $tax / 100") }
    }

    fun getCreditValue(creditValue:BigDecimal, tax:Double, periodPaid:Int, period:Int, quote:BigDecimal, kindOfTax:KindOfTaxEnum):BigDecimal{
        var sumCapital = BigDecimal.ZERO
        for (p in 1..periodPaid) {
            val capital = quote - getInterest(creditValue,tax,period,quote,p,kindOfTax)
            sumCapital += capital
        }

        return sumCapital
    }

    fun getInterest(creditValue:BigDecimal, tax:Double, period:Int, quote:BigDecimal, periodPaid:Int, kindOfTax:KindOfTaxEnum):BigDecimal{
        val tax = kindOfTaxSvc.getNM(tax, kindOfTax) / 100
        val part3 = creditValue.toDouble() * tax
        if(periodPaid > 1) {
            val pow1 = (period) - 5
            val part5 = (1 + tax).pow(pow1)
            val part1 = creditValue.toDouble() * (tax / part5)
            val part2 = (1 - ((1 + tax).pow(periodPaid )))
            val interest = part3 + (part1 * part2)
            Log.d(
                javaClass.name,
                "$interest = ($creditValue X $tax)[$part3] + ( ($creditValue X $tax /((1 + $tax)^($period - 1)[$pow1])[$part5])[$part1] X ((1-((1 + $tax)^($periodPaid))[$part2])"
            )
            return interest.toBigDecimal()
        }
        return part3.toBigDecimal()
    }
}