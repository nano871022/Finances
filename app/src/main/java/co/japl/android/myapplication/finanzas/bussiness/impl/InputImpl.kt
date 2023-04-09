package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.InputMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.PaidMap
import co.japl.android.myapplication.utils.DatabaseConstants
import java.util.*

class InputImpl(val view:View,override var dbConnect: SQLiteOpenHelper) : SaveSvc<InputDTO>,ISaveSvc<InputDTO> {
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
        val cursor = db.query(InputDB.Entry.TABLE_NAME,COLUMNS,"",arrayOf(),null,null,null,null)
        val items = mutableListOf<InputDTO>()
        val mapper = InputMap(view)
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: InputDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = InputMap(view).mapping(dto)
        return if(dto.id > 0){
            db?.update(InputDB.Entry.TABLE_NAME,content,"id=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(InputDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<InputDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(InputDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<InputDTO>()
        val mapper = InputMap(view)
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
            InputDB.Entry.TABLE_NAME,COLUMNS,"id = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = InputMap(view)
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