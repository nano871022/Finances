package co.japl.android.myapplication.bussiness.impl

import android.content.ContentValues
import co.japl.android.myapplication.bussiness.DTO.CalcDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.SaveSvc
import co.japl.android.myapplication.bussiness.impl.mapping.CalcMap

class SaveImpl(override var dbConnect: DBConnect) : SaveSvc {

    override fun save(calc: CalcDTO): Boolean {
        var db = dbConnect.writableDatabase
        val dto = CalcMap().mapping(calc)
        return db?.insert(CalcDB.CalcEntry.TABLE_NAME,null,dto)!! > 0
    }
}