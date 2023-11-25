package co.japl.finances.core.adapters.outbound.interfaces

import java.math.BigDecimal

interface IPaidRecapPort {

    fun getTotalPaid(): BigDecimal
}