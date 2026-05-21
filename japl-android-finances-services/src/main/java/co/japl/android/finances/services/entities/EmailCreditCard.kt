package co.japl.android.finances.services.entities

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

data class EmailCreditCard(
    val id: Int?=null,
    val sender: String?=null,
    val subjectPattern: String?=null,
    val bodyPattern: String?=null,
    val codeCreditCard: Int?=null,
    val nameCreditCard: String?=null,
    val kindInterestRateEnum: KindInterestRateEnum?=null,
    val active: Boolean=false,
    val createDate: LocalDateTime= LocalDateTime.now()
)

object EmailCreditCardDB {
    object EmailCreditCardEntry {
        const val TABLE_NAME = "TB_EMAIL_CREDIT_CARD"
        const val COLUMN_SENDER = "str_sender"
        const val COLUMN_SUBJECT_PATTERN = "str_subject_pattern"
        const val COLUMN_BODY_PATTERN = "str_body_pattern"
        const val COLUMN_CODE_CREDIT_CARD = "cod_credit_card"
        const val COLUMN_NAME_CREDIT_CARD = "str_name_credit_card"
        const val COLUMN_KIND_INTEREST_RATE = "str_kind_interest_rate"
        const val COLUMN_ACTIVE = "num_active"
        const val COLUMN_CREATE_DATE = "dt_create"
    }
}
