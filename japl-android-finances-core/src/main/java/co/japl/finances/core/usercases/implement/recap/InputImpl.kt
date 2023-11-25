package co.japl.finances.core.usercases.implement.recap

import co.japl.finances.core.adapters.outbound.interfaces.IInputPort
import co.japl.finances.core.usercases.interfaces.recap.IInput
import java.math.BigDecimal
import javax.inject.Inject

class InputImpl @Inject constructor(private val inputSvc:IInputPort) : IInput {
    override fun getTotalInputs(): BigDecimal? {
        return inputSvc.getTotalInputs()
    }
}