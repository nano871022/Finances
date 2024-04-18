package co.japl.android.finances.services.dao.implement

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.CreditCardBoughtDB
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.dao.interfaces.ICheckQuoteDAO
import co.japl.android.finances.services.mapping.CheckQuoteMap
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import co.japl.android.finances.services.utils.DatabaseConstants
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CheckQuoteImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) :
    ICheckQuoteDAO {
    private val mapper = CheckQuoteMap()
    private val COLUMNS = arrayOf(
        BaseColumns._ID,
        CheckQuoteDB.Entry.COLUMN_DATE_CREATE,
        CheckQuoteDB.Entry.COLUMN_CHECK,
        CheckQuoteDB.Entry.COLUMN_PERIOD,
        CheckQuoteDB.Entry.COLUMN_COD_QUOTE
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCheckPayment(codPaid: Int, period: String): Optional<CheckQuoteDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CheckQuoteDB.Entry.TABLE_NAME,
            COLUMNS,
            " ${CheckQuoteDB.Entry.COLUMN_COD_QUOTE} = ? AND ${CheckQuoteDB.Entry.COLUMN_PERIOD} = ?",
            arrayOf(codPaid.toString(),period),
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                return Optional.ofNullable(mapper.mapping(this))
            }
        }
        return Optional.empty()
    }

    override fun getPeriodsPayment(): List<PeriodCheckPaymentsPOJO> {
        val list = ArrayList<PeriodCheckPaymentsPOJO>()
        val db = dbConnect.writableDatabase
        val sql = """
            SELECT ${CheckQuoteDB.Entry.COLUMN_PERIOD},
                   ${CheckQuoteDB.Entry.COLUMN_COD_QUOTE},
                   ${CheckQuoteDB.Entry.COLUMN_CHECK}
            FROM ${CheckQuoteDB.Entry.TABLE_NAME}  
            """
        val cursor = db.rawQuery(sql, arrayOf()
        )
        while(cursor.moveToNext()){
            val period = cursor.getString(0)
            val cod_credit_card = cursor.getInt(1)
            val check = cursor.getInt(2)
            list.add(PeriodCheckPaymentsPOJO("$period::$cod_credit_card::$check",0.0,0.0,1))
        }
        return list.also { Log.d(javaClass.name,"<<== Get Periods Payment $it ${it.size}") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CheckQuoteDTO): Long {
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        if(dto.id <= 0){
            return db.insert(CheckQuoteDB.Entry.TABLE_NAME,null,columns).also { Log.d(javaClass.name,"Save:: Insert $it") }
        }
        return  db.update(CheckQuoteDB.Entry.TABLE_NAME ,mapper.mapping(dto),"${BaseColumns._ID} = ?", arrayOf(dto.id.toString())  ).toLong().also { Log.d(javaClass.name,"Save:: Update $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CheckQuoteDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CheckQuoteDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CheckQuoteDB.Entry.TABLE_NAME,
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
        }catch(e: SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }finally {
            Log.d(this.javaClass.name, "<<<=== getAll - End")
        }
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(CheckQuoteDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CheckQuoteDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CheckQuoteDB.Entry.TABLE_NAME,
            COLUMNS,
            " ${BaseColumns._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                return Optional.ofNullable(mapper.mapping(this))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: CheckQuoteDTO): List<CheckQuoteDTO> {
        val db = dbConnect.readableDatabase
        val list = mutableListOf<CheckQuoteDTO>()
        var select = ""
        var selectArgs = mutableListOf<String>()
        if(values.period.isNotBlank()){
            select = " ${CheckPaymentsDB.Entry.COLUMN_PERIOD} = ?"
            selectArgs.add(values.period)
        }
        val cursor = db.query(
            CheckQuoteDB.Entry.TABLE_NAME,COLUMNS,select,
            selectArgs.toTypedArray(),null,null,null)
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).let(list::add)

            }
        }
        return list
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }
}