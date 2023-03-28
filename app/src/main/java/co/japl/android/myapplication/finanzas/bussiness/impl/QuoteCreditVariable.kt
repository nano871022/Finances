package co.japl.android.myapplication.bussiness.impl

import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import java.math.BigDecimal
import java.math.RoundingMode

class QuoteCreditVariable : Calc {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()

    override fun calc(value: BigDecimal, period: Long, tax: Double,kindOfTax:KindOfTaxEnum): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val response = if(period > 0){ value.divide(BigDecimal(period),2,RoundingMode.HALF_EVEN)}else{BigDecimal.ZERO}
        return response.also { Log.v(this.javaClass.name,"<<<=== FINISH:: Calc: $value / $period = $response") }
    }

    private fun getTax(tax:Double):Double{
        val interest = tax / 100
        return interest.also { Log.d(this.javaClass.name,"<<<=== FINISH:: getTax: $interest = $tax / 100") }
    }
}