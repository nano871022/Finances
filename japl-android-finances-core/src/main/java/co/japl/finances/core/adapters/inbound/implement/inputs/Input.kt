package co.japl.finances.core.adapters.inbound.implement.inputs

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.japl.finances.core.usercases.interfaces.inputs.IInput
import java.time.YearMonth
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

    override fun getById(id: Int): InputDTO? {
        return inputSvc.getById(id)
    }

    override fun create(input: InputDTO): Boolean {
        return inputSvc.create(input)
    }

    override fun update(input: InputDTO): Boolean {
        return inputSvc.update(input)
    }

    override fun getTotalInputs(codeAccount: Int, period: YearMonth): Double {
        return inputSvc.getTotalInputs(codeAccount,period)
    }
}