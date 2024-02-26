package co.japl.finances.core.model

import co.com.japl.finances.iports.enums.KindOfTaxEnum

data class Tax(
    val value:Double,
    val kind:KindOfTaxEnum
)
