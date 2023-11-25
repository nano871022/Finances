package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.enums.KindOfTaxEnum
import java.math.BigDecimal

interface Calc {

    fun calc(value:BigDecimal,period:Long,tax:Double,kindOf: KindOfTaxEnum):BigDecimal


}