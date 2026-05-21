package com.nano871022.finances.core.domain.model

import java.math.BigDecimal

data class TaxDeclaration(
    val fiscalYear: Int,
    val grossIncome: BigDecimal,
    val creditCardPurchases: BigDecimal,
    val totalConsumptions: BigDecimal,
    val grossPatrimony: BigDecimal,
    val taxableBase: BigDecimal
)

data class FiscalYear(val year: Int)

data class PatrimonyAsset(
    val id: Long?,
    val name: String,
    val value: BigDecimal,
    val type: String
)

data class TaxBracket(
    val minLimitUvt: BigDecimal,
    val maxLimitUvt: BigDecimal?,
    val marginalRate: Double,
    val baseOffsetUvt: BigDecimal
)
