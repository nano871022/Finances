package co.japl.android.myapplication.finanzas.pojo

import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum
import java.math.BigDecimal

data class AmortizationTable(
    val code:Long,
    val months:Short,
    val value: BigDecimal,
    val quotePaid:Short,
    val quote1NotPaid:Boolean,
    val differInstallment:Boolean,
    val kind:AmortizationKindOfEnum
    )
