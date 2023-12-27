package co.japl.android.finances.services.dao.implement

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.dao.interfaces.IInputSvc
import co.japl.android.finances.services.mapping.InputMap
import co.japl.android.finances.services.utils.DatabaseConstants
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
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
        InputDB.Entry.COLUMN_START_DATE,
        InputDB.Entry.COLUMN_END_DATE
    )
    private val FORMAT_DATE_INPUT_WHERE = "date(substr(${InputDB.Entry.COLUMN_DATE_INPUT},7,4)||'-'||substr(${InputDB.Entry.COLUMN_DATE_INPUT},4,2)||'-'||substr(${InputDB.Entry.COLUMN_DATE_INPUT},1,2))"
    private val FORMAT_DATE_END_WHERE = "substr(${InputDB.Entry.COLUMN_END_DATE},7,4)||'-'||substr(${InputDB.Entry.COLUMN_END_DATE},4,2)||'-'||substr(${InputDB.Entry.COLUMN_END_DATE},1,2)"
    private val FORMAT_DATE_END_WHERE2 = "substr(${InputDB.Entry.COLUMN_END_DATE},1,4)||'-'||substr(${InputDB.Entry.COLUMN_END_DATE},6,2)||'-'||substr(${InputDB.Entry.COLUMN_END_DATE},9,2)"

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
        val cursor = db.rawQuery("""
            SELECT SUM(value), count(1)
            FROM(
            SELECT
              CASE WHEN instr(endDate3,'/') == 0 then endDate3
                   WHEN instr(endDate,'/') == 0 then endDate
                   else endDate2
              END as endDate,
              value
                        FROM (
            SELECT 
                $FORMAT_DATE_END_WHERE as endDate,
                $FORMAT_DATE_END_WHERE2 as endDate3,
                ${InputDB.Entry.COLUMN_END_DATE} as endDate2,
                ${InputDB.Entry.COLUMN_VALUE} AS value 
            FROM ${InputDB.Entry.TABLE_NAME} 
            WHERE 
                 ${InputDB.Entry.COLUMN_KIND_OF} = 'Mensual'
                 )) WHERE endDate >= date('now')
        """.trimMargin(),
            arrayOf()
        )
        with(cursor){
            while(moveToNext()){

                val input = cursor.getDouble(0).toBigDecimal()
                val cnt = cursor.getInt(1)

                return input.also { Log.d(javaClass.name,"=== GetTotalInput Montlhy $cnt. $input.") }
            }
        }
        return BigDecimal.ZERO
    }


    override fun getTotalInputsSemestral(): BigDecimal {
        val db = dbConnect.readableDatabase
        val cursor = db.rawQuery("""
            SELECT
                SUM(${InputDB.Entry.COLUMN_VALUE}) AS value
                , COUNT(1) as cnt
            FROM ${InputDB.Entry.TABLE_NAME}
            WHERE
                ${InputDB.Entry.COLUMN_END_DATE} >= date('now')
                AND $FORMAT_DATE_INPUT_WHERE BETWEEN date('now','start of month') and date('now','start of month','+1 month') 
                AND ${InputDB.Entry.COLUMN_KIND_OF} = 'semestral'
                AND exists (SELECT 1 FROM ${AccountDB.Entry.TABLE_NAME} WHERE ${BaseColumns._ID} = ${InputDB.Entry.COLUMN_ACCOUNT_CODE})
              """.trimMargin(), arrayOf())
        with(cursor) {
            while (moveToNext()) {
                val input = cursor.getDouble(0).toBigDecimal()
                val cnt = cursor.getInt(1)
                Log.d(javaClass.name, "=== GetTotalInputSemestral $cnt. $input")
                return input
            }
        }
        return BigDecimal.ZERO
    }

    override fun getAllValid(accountCode:Int,date: LocalDate): List<InputDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(InputDB.Entry.TABLE_NAME,COLUMNS,"""
                ${InputDB.Entry.COLUMN_ACCOUNT_CODE} = ?
            """, arrayOf(accountCode.toString()),null,null,null)
        Log.d(javaClass.name,"=== GetAllValid $FORMAT_DATE_END_WHERE Date ${DateUtils.localDateToStringDate(date)}")
        val items = mutableListOf<InputDTO>()
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).takeIf { it.dateEnd > date }?.let {
                    Log.d(javaClass.name,"=== GetAllValid ${it.dateEnd} Date ${date} ${it}")
                    items.add(it)
                }}
        }
        return items
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