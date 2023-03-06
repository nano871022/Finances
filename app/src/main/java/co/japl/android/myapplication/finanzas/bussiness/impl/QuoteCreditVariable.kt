package co.japl.android.myapplication.bussiness.impl

import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.Calc
import java.math.BigDecimal
import java.math.RoundingMode

class QuoteCreditVariable : Calc {

    override fun calc(value: BigDecimal, period: Long, tax: Double): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val response = value.divide(BigDecimal(period),2,RoundingMode.HALF_EVEN)
        return response.also { Log.v(this.javaClass.name,"<<<=== FINISH:: Calc: $value / $period = $response") }
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        return interest.also { Log.d(this.javaClass.name,"<<<=== FINISH:: getTax: $interest = $tax / 100") }
    }
}