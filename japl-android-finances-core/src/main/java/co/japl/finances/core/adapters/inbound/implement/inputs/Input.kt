package co.japl.finances.core.adapters.inbound.implement.inputs

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.japl.finances.core.usercases.interfaces.inputs.IInput
import javax.inject.Inject

class InputImpl @Inject constructor(private val inputSvc:IInput): IInputPort {
    override fun getInputs(accountCode:Int): List<InputDTO> {
        return inputSvc.getInputs(accountCode)
    }

    override fun deleteRecord(id: Int): Boolean {
        return inputSvc.deleteRecord(id)
    }

    override fun updateValue(id: Int, value: Double): Boolean {
        return inputSvc.updateValue(id,value)
    }
}