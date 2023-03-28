package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal


data class AmortizationCreditFix (
    var order: Int,
    var currentValueCredit: BigDecimal,
    var interestValue: BigDecimal,
    var capitalValue: BigDecimal,
    var totalQuote: BigDecimal,
    var newCurrentValueCredit: BigDecimal,
    var additional:BigDecimal
        )