package co.japl.finances.core.adapters.inbound.implement.recap

import co.japl.finances.core.adapters.inbound.interfaces.recap.IInputPort
import co.japl.finances.core.usercases.interfaces.recap.IInput
import java.math.BigDecimal
import javax.inject.Inject

class InputImp @Inject constructor(private val inputImpl: IInput):IInputPort {
    override fun getTotalInputs(): BigDecimal? {
        return inputImpl.getTotalInputs()
    }
}