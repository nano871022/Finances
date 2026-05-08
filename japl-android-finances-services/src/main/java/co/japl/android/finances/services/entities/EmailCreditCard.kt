package co.japl.android.finances.services.entities

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

data class EmailCreditCard(
    var id: Int? = null,
    var sender: String? = null,
    var subjectPattern: String? = null,
    var bodyPattern: String? = null,
    var codeCreditCard: Int? = null,
    var nameCreditCard: String? = null,
    var kindInterestRateEnum: KindInterestRateEnum? = null,
    var active: Boolean? = null,
    var create: LocalDateTime? = null
)

object EmailCreditCardDB {
    object Entry {
        const val TABLE_NAME = "TB_EMAIL_CREDIT_CARD"
        const val COLUMN_CODE_CREDIT_CARD = "nbr_credit_card"
        const val COLUMN_SENDER = "str_sender"
        const val COLUMN_SUBJECT_PATTERN = "str_subject_pattern"
        const val COLUMN_BODY_PATTERN = "str_body_pattern"
        const val COLUMN_KIND_INTEREST_RATE = "nbr_kind_interest_rate"
        const val COLUMN_ACTIVE = "nbr_active"
        const val COLUMN_CREATE_DATE = "dt_create"
    }
}
