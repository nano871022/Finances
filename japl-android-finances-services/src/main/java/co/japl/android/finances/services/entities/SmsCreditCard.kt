package co.japl.android.finances.services.entities

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDate
import java.time.LocalDateTime

data class SmsCreditCard(
    var id:Int?=null,
    var phoneNumber:String?=null,
    var codeCreditCard:Int?=null,
    var nameCreditCard:String?=null,
    var pattern:String?=null,
    var kindInterestRateEnum: KindInterestRateEnum?=null,
    var active:Boolean?=null,
    var create:LocalDateTime?=null
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
