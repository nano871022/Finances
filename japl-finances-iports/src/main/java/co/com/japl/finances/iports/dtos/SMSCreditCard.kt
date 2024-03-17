package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindInterestRateEnum

data class SMSCreditCard(
    val id:Int,
    val phoneNumber:String,
    val codeCreditCard:Int,
    val nameCreditCard:String,
    val pattern:String,
    val kindInterestRateEnum: KindInterestRateEnum,
    val active:Boolean
)
