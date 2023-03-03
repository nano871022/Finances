package co.japl.android.myapplication.bussiness.impl

import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.CalcInterest
import java.math.BigDecimal
import java.math.RoundingMode

class QuoteCreditVariableInterestQuote : CalcInterest {

    override fun calc(value: BigDecimal, period: Long, tax: Double,quoteNumber:Long): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val quote = value.divide(BigDecimal(period),2,RoundingMode.HALF_UP)
        val paid = quote.multiply(BigDecimal(quoteNumber - 1))
        val valueCurrent = value.minus(paid)
        val taxDouble = getTax(tax)
        return valueCurrent.multiply(taxDouble.toBigDecimal()).also { Log.d(this.javaClass.name,"<<<=== FINISH:: Calc: $valueCurrent X $taxDouble") }
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        return interest.also { Log.d(this.javaClass.name,"<<<=== FINISH:: getTax: $interest = $tax / 100") }
    }
}