package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class PeriodCreditDTO (
    var date:LocalDate,
    var count:Int,
    var value:BigDecimal
        )