package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.PaidMap
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
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
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null,null)
        val items = mutableListOf<PaidDTO>()
        val maxDate = values.date.plusMonths(1).minusDays(1)
        Log.d(javaClass.name,"Range ${values.date} $maxDate")
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                val dto = mapper.mapping(cursor)
                if(dto.date >= values.date && dto.date <= maxDate) {
                    items.add(dto)
                }
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRecurrent(date:LocalDate):List<PaidDTO>{
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null,null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                val dto = mapper.mapping(cursor)
                if(dto.date < date && dto.recurrent == (1).toShort()) {
                    items.add(dto)
                }
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: PaidDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = PaidMap().mapping(dto)
        Log.d(javaClass.name,"${dto.id} ${dto.date} ${dto.recurrent}")
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

    private fun getPaid(date:LocalDate):PaidDTO{
        val id = 0
        val account = ""
        val name = ""
        val value = BigDecimal.ZERO
        val recurrent = 0
        return PaidDTO(id,date, account,name,value,recurrent.toShort())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPaids(date:LocalDate):PaidsPOJO{
        val list = get(getPaid(date)).toMutableList()
        getRecurrent(date)?.let{
            if(it.isNotEmpty()){
                list.addAll(it)
            }
        }
        val current = LocalDate.now()
        val period = String.format("%4d%02d",current.year,current.monthValue).toInt()
        val count = list?.size ?: 0
        val paid = getPaid(list)
        return PaidsPOJO(period,paid,count)
    }

    private fun getPaid(list:List<PaidDTO>):Double{
        return list?.let {
            if(list.isNotEmpty()) {
                list?.map { it.value }?.reduce { acc, value -> acc + value }?.toDouble() ?: 0.0
            }else {
                0.0
            }
        } ?: 0.0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPeriods():List<PaidDTO>{
        val db = dbConnect.readableDatabase
        val cursor = db.rawQuery("select max(${PaidDB.Entry.COLUMN_DATE_PAID}) from ${PaidDB.Entry.TABLE_NAME}",null)
        val list = mutableListOf<PaidDTO>()
        with(cursor){
            while(moveToNext()){
                val datePaid = cursor.getString(0)
                datePaid?.let{
               var date = DateUtils.toLocalDate(datePaid)
                Log.d(javaClass.name,"Periods  $date")
                date = date.withDayOfMonth(1)
                val periods = Period.between(date,LocalDate.now())
                Log.d(javaClass.name,"Periods $periods $date")
                for( i in 0..periods.months) {
                    val dateFound = date.plusMonths(i.toLong())
                    val records = get(getPaid(dateFound)).toMutableList()
                    getRecurrent(dateFound)?.let {
                        if (it.isNotEmpty()) {
                            records.addAll(it)
                        }
                    }
                    val value = if (records.isNotEmpty()) records.map { it.value }
                        .reduce { acc, value -> acc + value } else BigDecimal.ZERO
                    list.add(getPeriodPaidDTO(dateFound, value))
                }
                }
            }
        }
        Log.d(javaClass.name,"Periods Records ${list.size}")
        return list.sortedByDescending { it.date }
    }

    private fun getPeriodPaidDTO(date:LocalDate,value:BigDecimal):PaidDTO{
        val id = 0
        val account = ""
        val name = ""
        val recurrent = 0
        return PaidDTO(id,date,account,name,value,recurrent.toShort())
    }


    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }
}