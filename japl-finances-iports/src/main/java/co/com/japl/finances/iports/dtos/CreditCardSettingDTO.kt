package co.com.japl.finances.iports.dtos

import java.time.LocalDateTime

data class CreditCardSettingDTO (
    var id:Int,
    var codeCreditCard:Int,
    var name:String,
    var value: String,
    var type: String,
    var create: LocalDateTime,
    var active: Short
    )
