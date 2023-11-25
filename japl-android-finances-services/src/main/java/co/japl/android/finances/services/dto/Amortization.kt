package co.japl.android.finances.services.dto

import java.math.BigDecimal


data class Amortization (
    var order: Int,
    var currentValueCredit: BigDecimal,
    var interestValue: BigDecimal,
    var capitalValue: BigDecimal,
    var totalQuote: BigDecimal,
    var newCurrentValueCredit: BigDecimal
        )