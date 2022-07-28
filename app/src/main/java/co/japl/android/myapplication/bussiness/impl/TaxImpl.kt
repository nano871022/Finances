package co.japl.android.myapplication.bussiness.impl

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.TaxDB
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IMapper
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.TaxMap
import co.japl.android.myapplication.utils.DatabaseConstants

class TaxImpl(override var dbConnect: SQLiteOpenHelper) :  SaveSvc<TaxDTO>{
    private val COLUMNS = arrayOf(BaseColumns._ID,
                                  TaxDB.TaxEntry.COLUMN_TAX,
                                  TaxDB.TaxEntry.COLUMN_MONTH,
                                  TaxDB.TaxEntry.COLUMN_YEAR,
                                  TaxDB.TaxEntry.COLUMN_status,
                                  TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD,
                                  TaxDB.TaxEntry.COLUMN_CREATE_DATE)
    private val mapper:IMapper<TaxDTO> = TaxMap()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: TaxDTO): Boolean {
        Log.i(this.javaClass.name,"<<<== save - Start")
        try{
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        return db.insert(TaxDB.TaxEntry.TABLE_NAME,null,columns) > 0
        }finally{
            Log.i(this.javaClass.name,"<<<=== save - End ")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<TaxDTO> {
        Log.i(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<TaxDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                TaxDB.TaxEntry.TABLE_NAME,
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
        Log.i(this.javaClass.name,"<<<=== delete - Start - $id")
        try{
        val db = dbConnect.writableDatabase
        return db.delete(TaxDB.TaxEntry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
        }finally {
            Log.i(this.javaClass.name, "<<<=== delete - End - $id")
        }
    }

}