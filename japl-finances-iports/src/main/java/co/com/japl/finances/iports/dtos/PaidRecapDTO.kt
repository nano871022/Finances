package co.com.japl.finances.iports.dtos

import java.time.Period
import java.time.YearMonth

data class PaidRecapDTO(
    val date:YearMonth,
    val count:Int,
    val totalPaid:Double
)
