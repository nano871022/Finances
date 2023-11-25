package co.japl.finances.core.dto

import java.time.LocalDateTime

data class BuyCreditCardSettingDTO (
    var id:Int,
    var codeBuyCreditCard:Int,
    var codeCreditCardSetting:Int,
    var create: LocalDateTime,
    var active: Short
    )