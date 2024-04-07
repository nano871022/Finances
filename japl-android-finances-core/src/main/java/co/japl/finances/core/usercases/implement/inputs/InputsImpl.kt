package co.japl.finances.core.usercases.implement.inputs

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.outbounds.IInputPort
import co.japl.finances.core.usercases.interfaces.inputs.IInput
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
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
        return input?.copy(dateEnd = LocalDate.now().withDayOfMonth(1).minusDays(1))?.let {currentDTO->
            inputSvc.update(currentDTO).takeIf { it }?.let {
                input.copy(value = value.toBigDecimal(), id = 0, dateEnd = LocalDate.MAX)?.let {newRecord->
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

    override fun getTotalInputs(codeAccount: Int, period: YearMonth): Double {
        return   inputSvc.getInputs(codeAccount).takeIf { it.isNotEmpty() }?.map { input->
            when(input.kindOf){
                "semestral"-> {
                    val date = YearMonth.of(input.date.year,input.date.monthValue)
                    if(date == period){
                        return input.value.toDouble()
                    }else if(date < period){
                        val date = LocalDate.of(date.year,date.monthValue,1)
                        val period = LocalDate.of(date.year,date.monthValue,1)
                        Period.between(date,period).takeIf { it.months % 6 == 0 }?.let{
                           input.value.toDouble()
                        }?:0.0
                    }else{
                        0.0
                    }
                 }
                else->input.value.toDouble()
            }
        }?.sumOf{ it }?:0.0
    }
}