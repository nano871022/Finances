package co.japl.finances.core.adapters.inbound.interfaces.recap

import java.math.BigDecimal

interface IPaidPort {
    fun getTotalPaid():BigDecimal
}