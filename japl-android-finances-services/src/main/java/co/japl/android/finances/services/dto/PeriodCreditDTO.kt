package co.japl.android.finances.services.dto

import java.math.BigDecimal
import java.time.LocalDate

data class PeriodCreditDTO (
    var date:LocalDate,
    var count:Int,
    var value:BigDecimal
        )