package co.japl.android.myapplication.bussiness.impl

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CreditCardMap
import co.japl.android.myapplication.utils.DatabaseConstants
import java.lang.RuntimeException
import java.sql.SQLException

class CreditCardImpl(override var dbConnect: SQLiteOpenHelper) :  SaveSvc<CreditCardDTO>{
    private val COLUMNS = arrayOf(BaseColumns._ID,
                                  CreditCardDB.CreditCardEntry.COLUMN_NAME,
                                  CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY,
                                  CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE,
                                  CreditCardDB.CreditCardEntry.COLUMN_STATUS,
                                  CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE)
    private val mapper = CreditCardMap()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardDTO): Boolean {
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        return db.insert(CreditCardDB.CreditCardEntry.TABLE_NAME,null,columns) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CreditCardDTO> {
        Log.i(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CreditCardDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CreditCardDB.CreditCardEntry.TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                null
            )
            with(cursor) {
                while (moveToNext()) {
                    list.add(mapper.mapping(this))
                }
            }
            return list
        }catch(e:SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }finally {
            Log.i(this.javaClass.name, "<<<=== getAll - End")
        }
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(CreditCardDB.CreditCardEntry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

}