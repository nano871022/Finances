package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.database.getDoubleOrNull
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IInputSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.InputMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.PaidMap
import co.japl.android.myapplication.utils.DatabaseConstants
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class InputImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper,public var mapper : InputMap) : IInputSvc{
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        InputDB.Entry.COLUMN_DATE_INPUT,
        InputDB.Entry.COLUMN_ACCOUNT_CODE,
        InputDB.Entry.COLUMN_KIND_OF,
        InputDB.Entry.COLUMN_NAME,
        InputDB.Entry.COLUMN_VALUE,
    )



    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: InputDTO): List<InputDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(InputDB.Entry.TABLE_NAME,COLUMNS,"${InputDB.Entry.COLUMN_ACCOUNT_CODE}=?",arrayOf(values.accountCode.toString()),null,null,null,null)
        val items = mutableListOf<InputDTO>()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTotalInputs(): BigDecimal {
        val db = dbConnect.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(${InputDB.Entry.COLUMN_VALUE}) AS value , COUNT(1) as cnt FROM ${InputDB.Entry.TABLE_NAME} WHERE ${InputDB.Entry.COLUMN_END_DATE} >= date('now') and ${InputDB.Entry.COLUMN_ACCOUNT_CODE} > 0 and ${InputDB.Entry.COLUMN_KIND_OF} = 'Mensual'",
            arrayOf()
        )
        with(cursor){
            while(moveToNext()){
                val input = cursor.getDouble(0).toBigDecimal()
                val cnt = cursor.getInt(1)
                Log.d(javaClass.name,"$cnt. $input")
                return input
            }
        }
        return BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: InputDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = mapper.mapping(dto)
        return if(dto.id > 0){
            db?.update(InputDB.Entry.TABLE_NAME,content,"${BaseColumns._ID}=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(InputDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<InputDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(InputDB.Entry.TABLE_NAME,COLUMNS,"${InputDB.Entry.COLUMN_ACCOUNT_CODE} > 0",null,null,null,null)
        val items = mutableListOf<InputDTO>()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(
            InputDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<InputDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            InputDB.Entry.TABLE_NAME,COLUMNS,"${BaseColumns._ID} = ?",
            arrayOf(id.toString()),null,null,null)
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(mapper.mapping(cursor))
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