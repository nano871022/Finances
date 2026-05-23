package co.com.japl.finances.iports.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class TaxDeclarationDTO(
    val fiscalYear: Int,
    val grossIncome: BigDecimal,
    val creditCardPurchases: BigDecimal,
    val totalConsumptions: BigDecimal,
    val grossPatrimony: BigDecimal,
    val taxableBase: BigDecimal,
    val taxValueCOP: BigDecimal,
    val isObligatedToFile: Boolean,
    val reasons: List<String>
)

data class TaxThresholdDTO(
    val year: Int,
    val uvtValue: BigDecimal,
    val incomeThreshold: BigDecimal,
    val consumptionThreshold: BigDecimal,
    val creditCardThreshold: BigDecimal,
    val wealthThreshold: BigDecimal
)

data class TaxProjectionDTO(
    val year: Int,
    val currentYTD: BigDecimal,
    val projectedEndOfYear: BigDecimal,
    val threshold: BigDecimal,
    val limitStatus: LimitStatus,
    val creditDebt: BigDecimal = BigDecimal.ZERO,
    val creditCardDebt: BigDecimal = BigDecimal.ZERO
)

enum class LimitStatus {
    SAFE, WARNING, EXCEEDED
}

data class TaxHistoryDTO(
    val id: Long,
    val date: LocalDate,
    val fiscalYear: Int,
    val taxValueCOP: BigDecimal
)

data class PatrimonyAssetDTO(
    val id: Long?,
    val name: String,
    val value: BigDecimal,
    val type: String
)
