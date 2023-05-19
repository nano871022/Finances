package co.japl.android.myapplication.bussiness.interfaces

import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import java.math.BigDecimal

interface Calc {

    fun calc(value:BigDecimal,period:Long,tax:Double,kindOf: KindOfTaxEnum):BigDecimal


}