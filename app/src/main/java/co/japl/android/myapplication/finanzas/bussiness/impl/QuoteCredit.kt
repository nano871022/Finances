package co.japl.android.myapplication.bussiness.impl

import co.japl.android.myapplication.bussiness.interfaces.Calc
import java.math.BigDecimal
import kotlin.math.pow

class QuoteCredit : Calc {

    override fun calc(value: BigDecimal, period: Long, tax: Double): BigDecimal {
        println("Credit Value $value")
        val taxDouble = getTax(tax)
        val onePlusTaxPowPeriod = onePlusTaxPowPeriod(taxDouble,period)
        val taxAndPeriodDiv = (taxDouble*onePlusTaxPowPeriod).div(onePlusTaxPowPeriod-1)
        val response = value.multiply(taxAndPeriodDiv.toBigDecimal())
        println("$response = $value X (($taxDouble X $onePlusTaxPowPeriod) / ($onePlusTaxPowPeriod - 1))")
        return response
    }

    private fun onePlusTaxPowPeriod(tax:Double,period:Long):Double{
        val response = (1+tax).pow(period.toDouble())
        println("$response = 1 + $tax ^ $period")
        return response
    }
    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        println("$interest = $tax / 100")
        return interest
    }
}