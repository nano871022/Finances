package co.japl.android.finances.services.dto

import java.time.LocalDate

data class AccountDTO(
    val id:Int,
    val create:LocalDate,
    val name:String,
    val active:Boolean
)

object AccountDB{
    object Entry{
        const val TABLE_NAME = "TB_ACCOUNT"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_ACTIVE = "nbr_active"
    }
}
