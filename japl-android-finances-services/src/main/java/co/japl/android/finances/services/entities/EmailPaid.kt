package co.japl.android.finances.services.entities

import java.time.LocalDateTime

data class EmailPaid(
    val id: Int? = null,
    val sender: String? = null,
    val subjectPattern: String? = null,
    val bodyPattern: String? = null,
    val codeAccount: Int? = null,
    val nameAccount: String? = null,
    val active: Boolean = false,
    val createDate: LocalDateTime = LocalDateTime.now()
)

object EmailPaidDB {
    object EmailPaidEntry {
        const val TABLE_NAME = "TB_EMAIL_PAID"
        const val COLUMN_SENDER = "str_sender"
        const val COLUMN_SUBJECT_PATTERN = "str_subject_pattern"
        const val COLUMN_BODY_PATTERN = "str_body_pattern"
        const val COLUMN_CODE_ACCOUNT = "cod_account"
        const val COLUMN_NAME_ACCOUNT = "str_name_account"
        const val COLUMN_ACTIVE = "num_active"
        const val COLUMN_CREATE_DATE = "dt_create"
    }
}
