package co.japl.android.finances.services.dto

import java.math.BigDecimal
import java.time.LocalDate

data class InputDTO(
    val id:Int,
    val date:LocalDate,
    val accountCode:Int,
    val kindOf:String,
    val name:String,
    val value:BigDecimal,
    val dateStart: LocalDate,
    val dateEnd: LocalDate
)

object InputDB{
    object Entry{
        const val TABLE_NAME = "TB_INPUT"
        const val COLUMN_DATE_INPUT = "dt_input"
        const val COLUMN_ACCOUNT_CODE = "cd_account"
        const val COLUMN_KIND_OF = "cd_kindof"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_VALUE = "nbr_value"
        const val COLUMN_START_DATE = "dt_start"
        const val COLUMN_END_DATE = "dt_end"
    }
}
