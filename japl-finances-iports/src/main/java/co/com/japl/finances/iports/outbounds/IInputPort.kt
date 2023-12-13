package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.InputDTO
import java.math.BigDecimal

interface IInputPort {

    fun getTotalInputs():BigDecimal?

    fun getInputs(): List<InputDTO>

    fun deleteRecord(id: Int): Boolean

    fun update(input:InputDTO): Boolean

    fun create(input:InputDTO):Int

    fun getInput(id:Int): InputDTO?

}