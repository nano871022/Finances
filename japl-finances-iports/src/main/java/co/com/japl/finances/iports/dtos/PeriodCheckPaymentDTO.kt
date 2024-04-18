package co.com.japl.finances.iports.dtos

import java.time.YearMonth

data class PeriodCheckPaymentDTO (
    val period: YearMonth,
    val count:Int,
    val amount:Double,
    val paid:Double
)