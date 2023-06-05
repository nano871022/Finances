package co.japl.android.myapplication.finanzas.bussiness.impl

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CreditCardMap
import co.japl.android.myapplication.bussiness.mapping.CreditCardSettingMap
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSettingSvc
import co.japl.android.myapplication.utils.DatabaseConstants
import java.util.*

class CreditCardSettingImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<CreditCardSettingDTO> ,ICreditCardSettingSvc{
    private val COLUMNS = arrayOf(
        BaseColumns._ID,
        CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD,
        CreditCardSettingDB.CreditCardEntry.COLUMN_NAME,
        CreditCardSettingDB.CreditCardEntry.COLUMN_VALUE,
        CreditCardSettingDB.CreditCardEntry.COLUMN_TYPE,
        CreditCardSettingDB.CreditCardEntry.COLUMN_ACTIVE,
        CreditCardSettingDB.CreditCardEntry.COLUMN_CREATE_DATE)
    private val mapper = CreditCardSettingMap()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardSettingDTO): Long {
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        if(dto.id <= 0){
            return db.insert(CreditCardSettingDB.CreditCardEntry.TABLE_NAME,null,columns)
        }
        return  db.update(CreditCardSettingDB.CreditCardEntry.TABLE_NAME ,mapper.mapping(dto),"_id = ?", arrayOf(dto.id.toString())  ) .toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CreditCardSettingDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CreditCardSettingDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CreditCardSettingDB.CreditCardEntry.TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                null
            )
            with(cursor) {
                while (moveToNext()) {
                    mapper.mapping(this).ifPresent(list::add)
                }
            }
            return list
        }catch(e: SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }finally {
            Log.d(this.javaClass.name, "<<<=== getAll - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(codeCC:Int): List<CreditCardSettingDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CreditCardSettingDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CreditCardSettingDB.CreditCardEntry.TABLE_NAME,
                COLUMNS,
                "${CreditCardSettingDB.CreditCardEntry.COLUMN_COD_CREDIT_CARD} = ?",
                arrayOf(codeCC.toString()),
                null,
                null,
                null
            )
            with(cursor) {
                while (moveToNext()) {
                    mapper.mapping(this).ifPresent(list::add)
                }
            }
            return list.also { Log.d(this.javaClass.name, "<<<=== getAll - End") }
        }catch(e: SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(CreditCardSettingDB.CreditCardEntry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CreditCardSettingDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CreditCardSettingDB.CreditCardEntry.TABLE_NAME,
            COLUMNS,
            " ${BaseColumns._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            if (moveToNext()) {
                return mapper.mapping(this)
            }
        }
        return Optional.empty()
    }

    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }
}