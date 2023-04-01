package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.PaidMap
import co.japl.android.myapplication.utils.DatabaseConstants
import java.util.*

class PaidImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<PaidDTO>,ISaveSvc<PaidDTO> {
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        PaidDB.Entry.COLUMN_DATE_PAID,
        PaidDB.Entry.COLUMN_ACCOUNT,
        PaidDB.Entry.COLUMN_NAME,
        PaidDB.Entry.COLUMN_VALUE,
        PaidDB.Entry.COLUMN_RECURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: PaidDTO): List<PaidDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,COLUMNS,"",arrayOf(),null,null,null,null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: PaidDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = PaidMap().mapping(dto)
        return if(dto.id > 0){
            db?.update(PaidDB.Entry.TABLE_NAME,content,"id=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(PaidDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<PaidDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
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
            PaidDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<PaidDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            PaidDB.Entry.TABLE_NAME,COLUMNS,"id = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = PaidMap()
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