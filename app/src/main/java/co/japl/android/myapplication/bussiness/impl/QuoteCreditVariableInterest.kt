package co.japl.android.myapplication.bussiness.impl

import co.japl.android.myapplication.bussiness.Calc
import java.math.BigDecimal

class QuoteCreditVariableInterest : Calc{

    override fun calc(value: BigDecimal, period: Long, tax: Double): BigDecimal {
        println("Credit Value $value")
        val taxDouble = getTax(tax)
        return value.multiply(taxDouble.toBigDecimal())
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        println("$interest = $tax / 100")
        return interest
    }
}