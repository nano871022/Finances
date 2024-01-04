package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IDifferInstallment
import co.japl.android.myapplication.finanzas.bussiness.mapping.DifferInstallmentMap
import co.japl.android.myapplication.utils.DatabaseConstants
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class DifferInstallmentImpl @Inject constructor( override var dbConnect: SQLiteOpenHelper) : IDifferInstallment{
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        DifferInstallmentDB.Entry.COLUMN_DATE_CREATE,
        DifferInstallmentDB.Entry.COLUMN_CODE_QUOTE,
        DifferInstallmentDB.Entry.COLUMN_PENDING_VALUE_PAYABLE,
        DifferInstallmentDB.Entry.COLUMN_ORIGIN_VALUE,
        DifferInstallmentDB.Entry.COLUMN_NEW_INSTALLMENT,
        DifferInstallmentDB.Entry.COLUMN_OLD_INSTALLMENT,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: DifferInstallmentDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = DifferInstallmentMap().mapping(dto)
        return if(dto.id > 0){
            db?.update(DifferInstallmentDB.Entry.TABLE_NAME,content,"${BaseColumns._ID}=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(DifferInstallmentDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<DifferInstallmentDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(DifferInstallmentDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<DifferInstallmentDTO>()
        val mapper = DifferInstallmentMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(cutOff:LocalDate):List<DifferInstallmentDTO>{
        val items = mutableListOf<DifferInstallmentDTO>()
        val db = dbConnect.readableDatabase
        val formatDate = DateUtils.localDateToStringDate(cutOff)
        val cursor = db.query(DifferInstallmentDB.Entry.TABLE_NAME,COLUMNS
            ,"${DifferInstallmentDB.Entry.COLUMN_DATE_CREATE} <= ?"
            , arrayOf(formatDate)
            ,null,null,null)
        try {
            val mapper = DifferInstallmentMap()
            with(cursor) {
                while (moveToNext()) {
                    items.add(mapper.mapping(cursor))
                }
            }
        }catch(e: RuntimeException){
            Log.e(javaClass.name,e?.message!!)
        }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(
            DifferInstallmentDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<DifferInstallmentDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            DifferInstallmentDB.Entry.TABLE_NAME,COLUMNS,"${DifferInstallmentDB.Entry.COLUMN_CODE_QUOTE} = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = DifferInstallmentMap()
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