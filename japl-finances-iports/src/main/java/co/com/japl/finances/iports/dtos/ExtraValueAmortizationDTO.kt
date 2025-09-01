package co.com.japl.finances.iports.dtos

import java.time.LocalDate

data class ExtraValueAmortizationDTO(
    val id:Int,
    val code:Int,
    val numQuote:Long,
    val value:Double,
    val create: LocalDate
)
