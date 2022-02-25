package co.japl.android.myapplication.bussiness.impl

import co.japl.android.myapplication.bussiness.Calc
import co.japl.android.myapplication.bussiness.CalcInterest
import java.math.BigDecimal
import java.math.RoundingMode

class QuoteCreditVariableInterestQuote : CalcInterest{

    override fun calc(value: BigDecimal, period: Long, tax: Double,quoteNumber:Long): BigDecimal {
        println("Credit Value $value")
        val quote = value.divide(BigDecimal(period),2,RoundingMode.HALF_UP)
        val paid = quote.multiply(BigDecimal(quoteNumber - 1))
        val valueCurrent = value.minus(paid)
        val taxDouble = getTax(tax)
        println("$valueCurrent X $taxDouble")
        return valueCurrent.multiply(taxDouble.toBigDecimal())
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        println("$interest = $tax / 100")
        return interest
    }
}