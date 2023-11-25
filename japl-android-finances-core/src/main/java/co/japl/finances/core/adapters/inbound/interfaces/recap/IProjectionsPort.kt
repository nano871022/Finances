package co.japl.finances.core.adapters.inbound.interfaces.recap

import java.math.BigDecimal

interface IProjectionsPort {
    fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal>
}