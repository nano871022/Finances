package co.japl.android.finances.services.dto

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

