package co.com.japl.finances.iports.inbounds.inputs

import co.com.japl.finances.iports.dtos.InputDTO

interface IInputPort {

    fun getInputs(accountCode:Int):List<InputDTO>

    fun deleteRecord(id:Int):Boolean

    fun updateValue(id:Int, value:Double):Boolean
}