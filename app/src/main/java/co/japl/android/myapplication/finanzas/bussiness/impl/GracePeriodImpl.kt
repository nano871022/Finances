package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGracePeriod
import co.japl.android.myapplication.finanzas.bussiness.mapping.GracePeriodMap
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class GracePeriodImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IGracePeriod{
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        GracePeriodDB.Entry.COLUMN_DATE_CREATE,
        GracePeriodDB.Entry.COLUMN_DATE_END,
        GracePeriodDB.Entry.COLUMN_CODE_CREDIT,
        GracePeriodDB.Entry.COLUMN_PERIODS,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: GracePeriodDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = GracePeriodMap().mapping(dto)
        return if(dto.id > 0){
            db?.update(GracePeriodDB.Entry.TABLE_NAME,content,"${BaseColumns._ID}=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(GracePeriodDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<GracePeriodDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(GracePeriodDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<GracePeriodDTO>()
        val mapper = GracePeriodMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
   override fun get(codCredit:Long): List<GracePeriodDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(GracePeriodDB.Entry.TABLE_NAME,COLUMNS
            ,"${GracePeriodDB.Entry.COLUMN_CODE_CREDIT} = ?"
            , arrayOf(codCredit.toString())
            ,null,null,null)
        val items = mutableListOf<GracePeriodDTO>()
        val mapper = GracePeriodMap()
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
            GracePeriodDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<GracePeriodDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            GracePeriodDB.Entry.TABLE_NAME,COLUMNS,"${GracePeriodDB.Entry.COLUMN_CODE_CREDIT} = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = GracePeriodMap()
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(mapper.mapping(cursor))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int, date: LocalDate): Optional<GracePeriodDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            GracePeriodDB.Entry.TABLE_NAME,COLUMNS,
            """
                ${GracePeriodDB.Entry.COLUMN_CODE_CREDIT} = ?
                AND ${GracePeriodDB.Entry.COLUMN_DATE_CREATE} <= ?
                AND ${GracePeriodDB.Entry.COLUMN_DATE_END} >= ?
            """.trimMargin(),
            arrayOf(id.toString(),DateUtils.localDateToStringDate(date),DateUtils.localDateToStringDate(date)),null,null,null)
        val mapper = GracePeriodMap()
        with(cursor){
            while(moveToNext()){
                 return Optional.of(mapper.mapping(cursor))
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