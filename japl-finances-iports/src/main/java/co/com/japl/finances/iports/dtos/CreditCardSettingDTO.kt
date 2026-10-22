package co.com.japl.finances.iports.dtos

import java.time.LocalDateTime

data class CreditCardSettingDTO (
    var id: Int = 0,
    var codeCreditCard: Int = 0,
    var name: String = "",
    var value: String = "",
    var type: String = "",
    var create: LocalDateTime = LocalDateTime.now(),
    var active: Short = 0,
    var advanceValue: Double? = null
)
