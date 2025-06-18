package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class ProjectionRecap(
    val limitDate: LocalDate,
    val savedCash: BigDecimal,
    val monthsLeft: Short
)
