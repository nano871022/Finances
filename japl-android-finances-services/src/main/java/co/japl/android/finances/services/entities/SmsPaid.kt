package co.japl.android.finances.services.entities

import java.time.LocalDateTime

data class SmsPaid(
    var id:Int?=null,
    var phoneNumber:String?=null,
    var codeAccount:Int?=null,
    var nameAccount:String?=null,
    var pattern:String?=null,
    var active:Boolean?=null,
    var create:LocalDateTime?=null
)

object SmsPaidDB{
    object Entry{
        const val TABLE_NAME = "TB_SMS_PAID"
        const val COLUMN_CODE_ACCOUNT = "nbr_account"
        const val COLUMN_PHONE_NUMBER = "str_phone_num"
        const val COLUMN_PATTERN = "str_pattern"
        const val COLUMN_ACTIVE = "nbr_active"
        const val COLUMN_CREATE_DATE = "dt_create"
    }
}
