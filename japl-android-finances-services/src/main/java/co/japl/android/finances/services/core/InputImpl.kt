package co.japl.android.finances.services.core

import co.japl.android.finances.services.interfaces.IInputSvc
import co.com.japl.finances.iports.outbounds.IInputPort
import java.math.BigDecimal
import javax.inject.Inject

class InputImpl @Inject constructor(private val inputImpl:IInputSvc) : IInputPort {
    override fun getTotalInputs(): BigDecimal? {
        return inputImpl.getTotalInputs()
    }
}