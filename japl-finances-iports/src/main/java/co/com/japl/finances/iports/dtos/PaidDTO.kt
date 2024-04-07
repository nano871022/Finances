package co.com.japl.finances.iports.dtos

import java.time.LocalDate
import java.time.LocalDateTime

data class PaidDTO(
    val id: Int = 0,
    val itemName:String = "",
    val itemValue:Double = 0.0,
    val datePaid:LocalDateTime = LocalDateTime.now(),
    val account: Int,
    val recurrent: Boolean,
    val end: LocalDateTime = LocalDateTime.now()
)
