package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDateTime

class PeriodDTO(
    var creditCardId:Int,
    var periodStart: LocalDateTime,
    var periodEnd: LocalDateTime,
    var interest: BigDecimal,
    var capital: BigDecimal,
    var total: BigDecimal
        )

