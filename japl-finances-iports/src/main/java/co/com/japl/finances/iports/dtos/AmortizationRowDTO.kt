package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import java.math.BigDecimal

data class AmortizationRowDTO(
    val id:Short,
    val periods: Short,
    val creditRate: Double,
    val kindRate: KindOfTaxEnum,
    val creditValue: BigDecimal,
    val amortizatedValue:BigDecimal,
    val capitalValue: BigDecimal,
    val interestValue: BigDecimal,
    val quoteValue: BigDecimal
)