package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.outbounds.IInputPort
import co.japl.finances.core.usercases.interfaces.common.IInput
import java.math.BigDecimal
import javax.inject.Inject

class InputImpl @Inject constructor(private val inputSvc:IInputPort) : IInput {
    override fun getTotalInputs(): BigDecimal? {
        return inputSvc.getTotalInputs()
    }
}