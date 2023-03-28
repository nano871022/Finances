package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.AdditionalMap
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import co.japl.android.myapplication.utils.DatabaseConstants
import java.util.*

class AdditionalCreditImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<AdditionalCreditDTO>,ISaveSvc<AdditionalCreditDTO> {
    public  val COLUMNS = arrayOf(
        BaseColumns._ID,
        AdditionalCreditDB.Entry.COLUMN_NAME,
        AdditionalCreditDB.Entry.COLUMN_VALUE,
        AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE,
        AdditionalCreditDB.Entry.COLUMN_START_DATE,
        AdditionalCreditDB.Entry.COLUMN_END_DATE
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: AdditionalCreditDTO): Long {
        val db = dbConnect.writableDatabase
        val values:ContentValues? = AdditionalMap().mapping(dto)
        return (db?.insert(AdditionalCreditDB.Entry.TABLE_NAME,null,values) ?: 0).also {
            Log.d(javaClass.name,"<<<=== END SAVE Id Record: $it CreditCode: ${dto.creditCode}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<AdditionalCreditDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<AdditionalCreditDTO>()
        val mapper = AdditionalMap()
        with(cursor){
            while(moveToNext()){
                items.add( mapper.mapping(cursor))
            }
        }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(AdditionalCreditDB.Entry.TABLE_NAME,DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<AdditionalCreditDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS,"id = ?",
            arrayOf(id.toString()),null,null,null)
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(AdditionalMap().mapping(cursor))
            }
        }
        return Optional.empty()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: AdditionalCreditDTO): List<AdditionalCreditDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS,"${AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE} = ?", arrayOf(values.creditCode.toString()),null,null,null)
        val list = arrayListOf<AdditionalCreditDTO>()
        val mapper = AdditionalMap()
        with(cursor){
            while(moveToNext()){
                list.add( mapper.mapping(cursor) )
            }
        }
        return list.also { Log.d(javaClass.name,"<<<=== END Get $it CreditCard: ${values.creditCode}") }
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }
}