package co.japl.finances.core.usercases.interfaces.recap

import java.math.BigDecimal

interface IProjections {
    fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal>
}