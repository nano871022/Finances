package co.japl.android.myapplication.bussiness.impl

import android.content.ContentValues
import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.Calc
import co.japl.android.myapplication.bussiness.DTO.CalcDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.SaveSvc
import co.japl.android.myapplication.bussiness.impl.mapping.CalcMap
import co.japl.android.myapplication.utils.DatabaseConstants

class SaveImpl(override var dbConnect: DBConnect) : SaveSvc {
    private val COLUMNS_CALC = arrayOf(BaseColumns._ID
        ,CalcDB.CalcEntry.COLUMN_ALIAS
        ,CalcDB.CalcEntry.COLUMN_TYPE
        ,CalcDB.CalcEntry.COLUMN_INTEREST
        ,CalcDB.CalcEntry.COLUMN_PERIOD
        ,CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT
        ,CalcDB.CalcEntry.COLUMN_VALUE_CREDIT)

    override fun save(calc: CalcDTO): Boolean {
        var db = dbConnect.writableDatabase
        val dto = CalcMap().mapping(calc)
        return db?.insert(CalcDB.CalcEntry.TABLE_NAME,null,dto)!! > 0
    }

    override  fun getAll():List<CalcDTO>{
        var db = dbConnect.readableDatabase

        val cursor = db.query(CalcDB.CalcEntry.TABLE_NAME,COLUMNS_CALC,null,null,null,null,null)
        val items = mutableListOf<CalcDTO>()
        with(cursor){
            while(moveToNext()){
                items.add(CalcMap().mapping(this))
            }
        }
        return items
    }

    override fun delete(id:Int):Boolean{
        var db = dbConnect.writableDatabase
        return db.delete(CalcDB.CalcEntry.TABLE_NAME,DatabaseConstants.SQL_DELETE_CALC_ID, arrayOf(id.toString())) > 0
    }
}