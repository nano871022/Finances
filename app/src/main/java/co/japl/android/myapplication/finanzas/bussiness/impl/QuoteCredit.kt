package co.japl.android.myapplication.bussiness.impl

import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.Calc
import java.math.BigDecimal
import kotlin.math.pow

class QuoteCredit : Calc {

    override fun calc(value: BigDecimal, period: Long, tax: Double): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val taxDouble = getTax(tax)
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
}