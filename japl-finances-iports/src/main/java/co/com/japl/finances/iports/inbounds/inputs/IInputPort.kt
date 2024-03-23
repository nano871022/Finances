package co.com.japl.finances.iports.inbounds.inputs

import co.com.japl.finances.iports.dtos.InputDTO
import java.time.YearMonth

interface IInputPort {

    fun getInputs(accountCode:Int):List<InputDTO>

    fun deleteRecord(id:Int):Boolean

    fun updateValue(id:Int, value:Double):Boolean

    fun getById(id:Int):InputDTO?

    fun create(input:InputDTO):Boolean

    fun update(input:InputDTO):Boolean

    fun getTotalInputs(codeAccount:Int,period:YearMonth):Double


}