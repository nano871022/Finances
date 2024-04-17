package co.japl.android.finances.services.dao.implement

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.dao.interfaces.ICheckPaymentDAO
import co.japl.android.finances.services.mapping.CheckPaymentsMap
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import co.japl.android.finances.services.utils.DatabaseConstants
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CheckPaymentImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) :
    ICheckPaymentDAO {
    private val mapper = CheckPaymentsMap()
    private val COLUMNS = arrayOf(
        BaseColumns._ID,
        CheckPaymentsDB.Entry.COLUMN_DATE_CREATE,
        CheckPaymentsDB.Entry.COLUMN_CHECK,
        CheckPaymentsDB.Entry.COLUMN_PERIOD,
        CheckPaymentsDB.Entry.COLUMN_COD_PAID
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCheckPayment(codPaid: Int, period: String): Optional<CheckPaymentsDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CheckPaymentsDB.Entry.TABLE_NAME,
            COLUMNS,
            " ${CheckPaymentsDB.Entry.COLUMN_COD_PAID} = ? AND ${CheckPaymentsDB.Entry.COLUMN_PERIOD} = ?",
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
            SELECT ${CheckPaymentsDB.Entry.COLUMN_PERIOD},
                   SUM((SELECT ${PaidDB.Entry.COLUMN_VALUE} FROM ${PaidDB.Entry.TABLE_NAME} WHERE ${BaseColumns._ID} = ${CheckPaymentsDB.Entry.COLUMN_COD_PAID}) )AS value
                    , COUNT(1) AS CNT
            FROM ${CheckPaymentsDB.Entry.TABLE_NAME}  
            
            GROUP BY ${CheckPaymentsDB.Entry.COLUMN_PERIOD}
            """
        val cursor = db.rawQuery(sql, arrayOf()
        )
        while(cursor.moveToNext()){
            val period = cursor.getString(0)
            val paid = cursor.getDouble(1)
            val count = cursor.getLong(2)
            list.add(PeriodCheckPaymentsPOJO(period,paid,0.0,count))
        }
        return list.also { Log.d(javaClass.name,"<<== Get Periods Payment $it ${it.size}") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CheckPaymentsDTO): Long {
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        if(dto.id <= 0){
            return db.insert(CheckPaymentsDB.Entry.TABLE_NAME,null,columns).also { Log.d(javaClass.name,"Save:: Insert $it") }
        }
        return  db.update(CheckPaymentsDB.Entry.TABLE_NAME ,mapper.mapping(dto),"${BaseColumns._ID} = ?", arrayOf(dto.id.toString())  ).toLong().also { Log.d(javaClass.name,"Save:: Update $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CheckPaymentsDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CheckPaymentsDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CheckPaymentsDB.Entry.TABLE_NAME,
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
        return db.delete(CheckPaymentsDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CheckPaymentsDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CheckPaymentsDB.Entry.TABLE_NAME,
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
    override fun get(values: CheckPaymentsDTO): List<CheckPaymentsDTO> {
        val db = dbConnect.readableDatabase
        val list = mutableListOf<CheckPaymentsDTO>()
        var select = ""
        var selectArgs = mutableListOf<String>()
        if(values.period.isNotBlank()){
            select = " ${CheckPaymentsDB.Entry.COLUMN_PERIOD} = ?"
            selectArgs.add(values.period)
        }
        val cursor = db.query(
            CheckPaymentsDB.Entry.TABLE_NAME,
            COLUMNS,
            select,
            selectArgs.toTypedArray(),
            null,
            null,
            null)
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).let(list::add)
            }
        }
        return list.also {
            Log.d(javaClass.name,"<<== Get $it ")
        }
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }
}