package co.japl.android.myapplication.bussiness.impl

import co.japl.android.myapplication.bussiness.interfaces.Calc
import java.math.BigDecimal
import java.math.RoundingMode

class QuoteCreditVariable : Calc {

    override fun calc(value: BigDecimal, period: Long, tax: Double): BigDecimal {
        println("Credit Value $value")
        println("$value / $period")
        val response = value.divide(BigDecimal(period),2,RoundingMode.HALF_EVEN)
        return response
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        println("$interest = $tax / 100")
        return interest
    }
}