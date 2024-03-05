package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.InputDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IInputPort {

    fun getTotalInputs(cutOff:LocalDate):BigDecimal?

    fun getInputs(accountCode:Int): List<InputDTO>

    fun deleteRecord(id: Int): Boolean

    fun update(input:InputDTO): Boolean

    fun create(input:InputDTO):Int

    fun getInput(id:Int): InputDTO?

}