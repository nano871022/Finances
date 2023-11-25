package co.japl.android.finances.services.implement

import android.util.Log
import co.japl.android.finances.services.interfaces.Calc
import co.japl.android.finances.services.implement.KindOfTaxImpl
import co.japl.android.finances.services.interfaces.IKindOfTaxSvc
import co.japl.android.finances.services.enums.KindOfTaxEnum
import java.math.BigDecimal

class QuoteCreditVariableInterest : Calc {
    private val kindOfTaxSvc:IKindOfTaxSvc = KindOfTaxImpl()

    override fun calc(value: BigDecimal, period: Long, tax: Double,kindOf: KindOfTaxEnum): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START:: Calc: Credit Value $value")
        val taxValue = kindOfTaxSvc.getNM(tax,kindOf)
        return value.multiply(taxValue.toBigDecimal()).also { Log.d(javaClass.name,"<<<=== FINISH:: Calc: $it = $value X $taxValue ") }
    }
}