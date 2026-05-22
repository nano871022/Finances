package co.japl.android.finances.services.dto

import java.math.BigDecimal

data class PatrimonyDTO(
    val id: Long?,
    val name: String,
    val value: BigDecimal,
    val type: String
)

object PatrimonyDB {
    object PatrimonyEntry {
        const val TABLE_NAME = "dian_manual_patrimony_inputs"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_VALUE = "value"
        const val COLUMN_TYPE = "type"
    }
}
