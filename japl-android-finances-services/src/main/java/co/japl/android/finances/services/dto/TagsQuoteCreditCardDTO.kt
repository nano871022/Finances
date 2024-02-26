package co.japl.android.finances.services.dto

import java.time.LocalDate

data class TagsQuoteCreditCardDTO(
    val id:Int,
    val create:LocalDate,
    val codQuote:Int,
    val codTag:Int,
    val active:Boolean
)

object TagsQuoteCreditCardDB{
    object Entry{
        const val TABLE_NAME = "TB_TAG_QUOTE_CREDIT_CARD"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_CODE_QUOTE_CREDIT_CARD = "num_quotecc"
        const val COLUMN_CODE_TAG = "num_tag"
        const val COLUMN_ACTIVE = "nbr_active"
    }
}
