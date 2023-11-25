package co.japl.finances.core.adapters.inbound.interfaces.recap

import java.math.BigDecimal

interface IInputPort {

    fun getTotalInputs():BigDecimal?
}