package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class PeriodPaidDTO (
    val date:YearMonth = YearMonth.now(),
    val count:Int = 0,
    val value:Double = 0.0
)