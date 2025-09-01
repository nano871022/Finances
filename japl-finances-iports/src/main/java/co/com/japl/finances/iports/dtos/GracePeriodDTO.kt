package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class GracePeriodDTO(
    val id:Int,
    val create:LocalDate,
    val end:LocalDate,
    val codeCredit:Long,
    val periods:Short
)
