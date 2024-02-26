package co.japl.finances.core.usercases.implement.inputs

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.outbounds.IInputPort
import co.japl.finances.core.usercases.interfaces.inputs.IInput
import java.time.LocalDate
import javax.inject.Inject

class InputsImpl @Inject constructor(private val inputSvc:IInputPort) : IInput {
    override fun getInputs(accountCode:Int): List<InputDTO> {
        return inputSvc.getInputs(accountCode)
    }

    override fun deleteRecord(id: Int): Boolean {
        return inputSvc.deleteRecord(id)
    }

    override fun updateValue(id: Int, value: Double): Boolean {
        val input = inputSvc.getInput(id)
        return input?.copy(dateEnd = LocalDate.now().withDayOfMonth(1).minusDays(1))?.let {oldDTO->
            inputSvc.update(oldDTO).takeIf { it }?.let {
                input.copy(value = value.toBigDecimal(), id = 0)?.let {newRecord->
                    if(inputSvc.create(newRecord) > 0){
                        return true
                    }else{
                        inputSvc.update(input)
                        return false
                    }
                }
            }
        }?:false
    }

    override fun getById(id: Int): InputDTO? {
        return inputSvc.getInput(id)
    }

    override fun create(input: InputDTO): Boolean {
        return inputSvc.create(input) > 0
    }

    override fun update(input: InputDTO): Boolean {
        return inputSvc.update(input)
    }
}