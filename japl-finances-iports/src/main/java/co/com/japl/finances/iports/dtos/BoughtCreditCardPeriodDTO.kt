package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDateTime

data class BoughtCreditCardPeriodDTO(
    val creditCardId:Int,
    val cutoffDay:Short,
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val interest: BigDecimal,
    val capital: BigDecimal,
    val total: BigDecimal,
    val count:Long
)
