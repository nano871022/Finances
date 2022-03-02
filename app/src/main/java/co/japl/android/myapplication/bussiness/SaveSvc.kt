package co.japl.android.myapplication.bussiness

import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.DBConnect

interface SaveSvc  {
    var dbConnect:DBConnect

    fun save(calc:CalcDTO):Boolean

    fun getAll():List<CalcDTO>

    fun delete(id:Int):Boolean
}