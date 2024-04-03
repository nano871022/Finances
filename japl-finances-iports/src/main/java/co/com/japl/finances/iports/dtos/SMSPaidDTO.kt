package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindInterestRateEnum

data class SMSPaidDTO(
    val id:Int,
    val phoneNumber:String,
    val codeAccount:Int,
    val nameAccount:String,
    val pattern:String,
    val active:Boolean
)
