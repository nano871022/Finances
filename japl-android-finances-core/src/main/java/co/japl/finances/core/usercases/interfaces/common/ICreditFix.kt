package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal
import java.time.LocalDate

interface ICreditFix {
    fun getTotalQuote(date: LocalDate):BigDecimal
}