package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class InputDTO(
    val id:Int,
    val date:LocalDate,
    val accountCode:Int,
    val kindOf:String,
    val name:String,
    val value:BigDecimal,
)

object InputDB{
    object Entry{
        const val TABLE_NAME = "TB_INPUT"
        const val COLUMN_DATE_INPUT = "dt_input"
        const val COLUMN_ACCOUNT_CODE = "cd_account"
        const val COLUMN_KIND_OF = "cd_kindof"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_VALUE = "nbr_value"
    }
}
