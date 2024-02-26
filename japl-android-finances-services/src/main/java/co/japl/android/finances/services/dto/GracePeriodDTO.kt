package co.japl.android.finances.services.dto

import java.time.LocalDate

data class GracePeriodDTO(
    val id:Int,
    val create:LocalDate,
    val end:LocalDate,
    val codeCredit:Long,
    val periods:Short
)

object GracePeriodDB{
    object Entry{
        const val TABLE_NAME = "TB_GRACE_PERIOD"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_DATE_END = "dt_end"
        const val COLUMN_CODE_CREDIT = "cd_credit"
        const val COLUMN_PERIODS = "nbr_periods"
    }
}
