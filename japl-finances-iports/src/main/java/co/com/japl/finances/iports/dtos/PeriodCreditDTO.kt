package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth

data class PeriodCreditDTO (
    val date: YearMonth,
    val count:Int,
    val value:BigDecimal
)