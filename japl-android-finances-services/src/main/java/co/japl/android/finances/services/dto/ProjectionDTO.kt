package co.japl.android.finances.services.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProjectionDTO(
    var id:Int,
    var create:LocalDate,
    var end:LocalDate,
    var name:String,
    var type:String,
    var value:BigDecimal,
    var quote:BigDecimal,
    var active:Short
)

object ProjectionDB{
    object Entry{
        const val TABLE_NAME = "TB_PROJECTIONS"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_DATE_END = "dt_end"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_ACTIVE = "nbr_active"
        const val COLUMN_TYPE = "str_type"
        const val COLUMN_VALUE = "nbr_value"
        const val COLUMN_QUOTE = "nbr_quote"
    }
}
