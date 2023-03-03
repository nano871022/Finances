package co.japl.android.myapplication.bussiness.impl

import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.Calc
import java.math.BigDecimal

class QuoteCreditVariableInterest : Calc {

    override fun calc(value: BigDecimal, period: Long, tax: Double): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val taxDouble = getTax(tax)
        return value.multiply(taxDouble.toBigDecimal())
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        return interest.also { Log.d(this.javaClass.name,"<<<=== FINISH:: getTax: $interest = $tax / 100") }
    }
}