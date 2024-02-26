package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal

interface IProjections {
    fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal>
}