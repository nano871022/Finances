package co.japl.finances.core.usercases.interfaces.inputs

import co.com.japl.finances.iports.dtos.InputDTO

interface IInput {

    fun getInputs(accountCode:Int): List<InputDTO>

    fun deleteRecord(id: Int): Boolean

    fun updateValue(id: Int, value: Double): Boolean

    fun getById(id: Int): InputDTO?

    fun create(input: InputDTO): Boolean

    fun update(input: InputDTO): Boolean

}