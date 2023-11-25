package co.japl.finances.core.dto

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
