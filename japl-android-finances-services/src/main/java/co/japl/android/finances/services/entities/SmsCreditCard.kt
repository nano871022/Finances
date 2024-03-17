package co.japl.android.finances.services.entities

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDate
import java.time.LocalDateTime

data class SmsCreditCard(
    val id:Int,
    val phoneNumber:String,
    val codeCreditCard:Int,
    val nameCreditCard:String,
    val pattern:String,
    val kindInterestRateEnum: KindInterestRateEnum,
    val active:Boolean,
    val create:LocalDateTime
)

object SmsCreditCardDB{
    object Entry{
        const val TABLE_NAME = "TB_SMS_CREDIT_CARD"
        const val COLUMN_CODE_CREDIT_CARD = "nbr_credit_card"
        const val COLUMN_PHONE_NUMBER = "str_phone_num"
        const val COLUMN_PATTERN = "str_pattern"
        const val COLUMN_KIND_INTEREST_RATE = "nbr_kind_interest_rate"
        const val COLUMN_ACTIVE = "nbr_active"
        const val COLUMN_CREATE_DATE = "dt_create"
    }
}
