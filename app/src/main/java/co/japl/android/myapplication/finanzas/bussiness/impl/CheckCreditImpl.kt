package co.japl.android.myapplication.finanzas.bussiness.impl

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckPaymentSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CheckCreditMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.CheckPaymentsMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import co.japl.android.myapplication.utils.DatabaseConstants
import java.util.*
import kotlin.collections.ArrayList

class CheckCreditImpl(override var dbConnect: SQLiteOpenHelper) :ICheckCreditSvc {
    private val mapper = CheckCreditMap()
    private val COLUMNS = arrayOf(
        BaseColumns._ID,
        CheckCreditDB.Entry.COLUMN_DATE_CREATE,
        CheckCreditDB.Entry.COLUMN_CHECK,
        CheckCreditDB.Entry.COLUMN_PERIOD,
        CheckCreditDB.Entry.COLUMN_COD_CREDIT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCheckPayment(codPaid: Int, period: String): Optional<CheckCreditDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CheckCreditDB.Entry.TABLE_NAME,
            COLUMNS,
            " ${CheckCreditDB.Entry.COLUMN_COD_CREDIT} = ? AND ${CheckCreditDB.Entry.COLUMN_PERIOD} = ?",
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
            SELECT ${CheckCreditDB.Entry.COLUMN_PERIOD},
                   SUM((SELECT ${PaidDB.Entry.COLUMN_VALUE} FROM ${PaidDB.Entry.TABLE_NAME} WHERE ${BaseColumns._ID} = ${CheckCreditDB.Entry.COLUMN_COD_CREDIT}) )AS value
                    , COUNT(1) AS CNT
            FROM ${CheckCreditDB.Entry.TABLE_NAME}  
            
            GROUP BY ${CheckCreditDB.Entry.COLUMN_PERIOD}
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
    override fun save(dto: CheckCreditDTO): Long {
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        if(dto.id <= 0){
            return db.insert(CheckCreditDB.Entry.TABLE_NAME,null,columns).also { Log.d(javaClass.name,"Save:: Insert $it") }
        }
        return  db.update(CheckCreditDB.Entry.TABLE_NAME ,mapper.mapping(dto),"${BaseColumns._ID} = ?", arrayOf(dto.id.toString())  ).toLong().also { Log.d(javaClass.name,"Save:: Update $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CheckCreditDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CheckCreditDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CheckCreditDB.Entry.TABLE_NAME,
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
        return db.delete(CheckCreditDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CheckCreditDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CheckCreditDB.Entry.TABLE_NAME,
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
    override fun get(values: CheckCreditDTO): List<CheckCreditDTO> {
        val db = dbConnect.readableDatabase
        val list = mutableListOf<CheckCreditDTO>()
        val cursor = db.query(
            CheckCreditDB.Entry.TABLE_NAME,COLUMNS,null,
            null,null,null,null)
        with(cursor){
            while(moveToNext()){
                val value = mapper.mapping(cursor)
                if(value.date < values.date ) {
                    list.add(value)
                }
            }
        }
        return list
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }
}