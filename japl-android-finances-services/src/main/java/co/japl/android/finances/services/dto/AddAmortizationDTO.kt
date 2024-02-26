package co.japl.android.finances.services.dto

import java.time.LocalDate

data class AddAmortizationDTO(
    val id:Int,
    val create:LocalDate,
    val code:Int,
    val nbrQuote:Long,
    val value:Double
)

object AddAmortizationDB{
    object Entry{
        const val TABLE_NAME = "TB_ADD_AMORTIZATION"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_CODE = "nbr_code"
        const val COLUMN_NBR_QUOTE = "nbr_quote"
        const val COLUMN_VALUE = "nbr_value"
    }
}
