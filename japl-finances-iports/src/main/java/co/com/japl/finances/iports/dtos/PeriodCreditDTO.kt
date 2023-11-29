package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class PeriodCreditDTO (
    var date:LocalDate,
    var count:Int,
    var value:BigDecimal
        )