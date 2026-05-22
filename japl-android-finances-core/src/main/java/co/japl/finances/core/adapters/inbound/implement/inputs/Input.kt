package co.japl.finances.core.adapters.inbound.implement.inputs

import android.util.Log
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.japl.finances.core.usercases.interfaces.inputs.IInput
import java.time.YearMonth
import javax.inject.Inject

class InputImpl @Inject constructor(private val inputSvc:IInput): IInputPort {
    override fun getInputs(accountCode:Int): List<InputDTO> {
        require(accountCode > 0){"Accound Code is ZERO"}
        Log.d(javaClass.name,"=== GetInputs Account Code: $accountCode")
        return inputSvc.getInputs(accountCode)
    }

    override fun deleteRecord(id: Int): Boolean {
        require(id > 0){"Id Code is ZERO"}
        return inputSvc.deleteRecord(id)
    }

    override fun updateValue(id: Int, value: Double): Boolean {
        require(id > 0){"Id Code is ZERO"}
        require(value > 0.0){"Value is ZERO"}
        return inputSvc.updateValue(id,value)
    }

    override fun getById(id: Int): InputDTO? {
        require(id > 0){"Id Code is ZERO"}
        return inputSvc.getById(id)
    }

    override fun create(input: InputDTO): Boolean {
        require(input.id == 0){"Id Code is ZERO"}
        require(input.accountCode > 0){"Account Code is ZERO"}
        return inputSvc.create(input)
    }

    override fun update(input: InputDTO): Boolean {
        require(input.id > 0){"Id Code is ZERO"}
        require(input.accountCode > 0){"Account Code is ZERO"}
        return inputSvc.update(input)
    }

    override fun getTotalInputs(codeAccount: Int, period: YearMonth): Double {
        require(codeAccount > 0){"Account Code is ZERO"}
        return inputSvc.getTotalInputs(codeAccount,period)
    }
}