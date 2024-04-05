package co.japl.android.finances.services.dao.implement

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.interfaces.IGraph
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.japl.android.finances.services.mapping.PaidMap
import co.japl.android.finances.services.queries.PaidQuery
import co.japl.android.finances.services.utils.DatabaseConstants
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import java.util.*
import javax.inject.Inject

class PaidImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IPaidDAO, IGraph{
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        PaidDB.Entry.COLUMN_DATE_PAID,
        PaidDB.Entry.COLUMN_ACCOUNT,
        PaidDB.Entry.COLUMN_NAME,
        PaidDB.Entry.COLUMN_VALUE,
        PaidDB.Entry.COLUMN_RECURRENT,
        PaidDB.Entry.COLUMN_END_DATE
    )

    private val FORMAT_DATE_PAID = "substr(${PaidDB.Entry.COLUMN_DATE_PAID},7,4)||'-'||substr(${PaidDB.Entry.COLUMN_DATE_PAID},4,2)||'-'||substr(${PaidDB.Entry.COLUMN_DATE_PAID},1,2)"
    private val FORMAT_END_DATE = "substr(${PaidDB.Entry.COLUMN_END_DATE},7,4)||'-'||substr(${PaidDB.Entry.COLUMN_END_DATE},4,2)||'-'||substr(${PaidDB.Entry.COLUMN_END_DATE},1,2)"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: PaidDTO): List<PaidDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,
            COLUMNS,
            null,
            null,
            null,
            null,
            null,
            null)
        val items = mutableListOf<PaidDTO>()
        val maxDate = values.date.plusMonths(1).minusDays(1)
        Log.d(javaClass.name,"Range ${values.date} $maxDate")
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor)
                    .takeIf {it.date >= values.date && it.date <= maxDate && it.end >= maxDate}
                    ?.let{items.add(it)}
            }
        }
        return items
    }

    override fun findByNameValueDate(values: PaidDTO): List<PaidDTO> {
        val db = dbConnect.readableDatabase
        var selection:String? = null
        var selectionArgs: MutableList<String>? = null
        if(values.name.isNotEmpty()){
            selection = "${selection?:""} ${PaidDB.Entry.COLUMN_NAME} like ?"
            selectionArgs?.let{it.add("%${values.name.trim()}%")}?:selectionArgs.let{selectionArgs = mutableListOf("%${values.name.trim()}%") }
        }
        if(values.value > BigDecimal.ZERO){
            selection = "${selection?:""} AND ${PaidDB.Entry.COLUMN_VALUE} = ?"
            selectionArgs?.let{it.add("${values.value}")}?:selectionArgs.let{selectionArgs = mutableListOf("${values.value}") }
        }/*
        if (values.date != null) {
            selection = "${selection?:""} AND ${PaidDB.Entry.COLUMN_DATE_PAID} = ?"
            selectionArgs?.let{it.add("${values.date}")}?:selectionArgs.let{selectionArgs = mutableListOf("${values.date}") }
        }
        */
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,
            COLUMNS,
            selection,
            selectionArgs?.toTypedArray(),
            null,
            null,
            null,
            null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor)?.takeIf { it.date == values.date }
                    ?.let{items.add(it)}
            }
        }
        return items
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun getRecurrent(date:LocalDate):List<PaidDTO>{
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,
            COLUMNS,
            "${PaidDB.Entry.COLUMN_RECURRENT} = 1",
            null,
            null,
            null,
            null,
            null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).takeIf { it.date < date && it.end > date }?.let {  items.add(it) }
            }
        }
        return items.also { Log.d(javaClass.name,"Recurrent Size ${it.size}") }
    }

    override fun getAll(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        val startPeriod = LocalDate.of(period.year,period.monthValue,1)
        val endPeriod = startPeriod.plusMonths(1).minusDays(1)
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,
            COLUMNS,
            """
                ${PaidDB.Entry.COLUMN_ACCOUNT} like ?
                AND ${FORMAT_DATE_PAID} between ? and ?
            """.trimMargin(),
            arrayOf("%${codeAccount}%",DateUtils.localDateToStringDate(startPeriod),DateUtils.localDateToStringDate(endPeriod)),
            null,null,null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).takeIf {
                     it.date in startPeriod .. endPeriod
                }?.let(items::add)
            }
        }
        return items
    }

    override fun getRecurrents(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        val startPeriod = LocalDate.of(period.year,period.monthValue,1)
        val endPeriod = startPeriod.plusMonths(1).minusDays(1)
        val db = dbConnect.readableDatabase
        val cursor = db.query(PaidDB.Entry.TABLE_NAME,
            COLUMNS,
            """
                ${PaidDB.Entry.COLUMN_ACCOUNT} like ?
                AND ${PaidDB.Entry.COLUMN_RECURRENT} = 1
                AND ${FORMAT_END_DATE} > ?
            """.trimMargin(),
            arrayOf("%${codeAccount}%",DateUtils.localDateToStringDate(endPeriod)),
            null,null,null)
        val items = mutableListOf<PaidDTO>()
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).takeIf {
                    it.end >= endPeriod
                }?.let{
                    try {
                        items.add(
                            it.copy(
                                date = it.date.withMonth(period.monthValue).withYear(period.year)
                            )
                        )
                    }catch (e:Exception){
                        items.add(it)
                    }
                }
            }
        }
        return items
    }

    override fun getTotalPaid(date:LocalDate): BigDecimal {
        val db = dbConnect.readableDatabase
        val listValues = mutableListOf<Double>()

        val cursor = db.rawQuery("""
                SELECT 
                      ${PaidDB.Entry.COLUMN_VALUE} AS value
                    , ${PaidDB.Entry.COLUMN_RECURRENT} AS recurrent
                    , ${PaidDB.Entry.COLUMN_DATE_PAID} AS date_paid
                     , ${PaidDB.Entry.COLUMN_END_DATE} AS end_paid
                FROM ${PaidDB.Entry.TABLE_NAME} 
                """, arrayOf())
        with(cursor){
            while (moveToNext()){
                val recurrent = cursor.getInt(1)
                val datePaid = cursor.getString(2).let { DateUtils.toLocalDate(it) }
                val endDate = cursor.getString(3).let { DateUtils.toLocalDate(it) }
                val value = cursor.getDouble(0)
                if(((recurrent == 1 && endDate >= date.withDayOfMonth(1).plusMonths(1))||
                            datePaid in date.withDayOfMonth(1).. date.plusMonths(1).withDayOfMonth(1).minusDays(1)) &&
                            endDate >= date.withDayOfMonth(1).plusMonths(1)) {
                    listValues.add(value)
                }

            }
        }
        return listValues.sum().toBigDecimal().also{ Log.d(javaClass.name,"<<<=== GetTotalPaid Total $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: PaidDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = PaidMap().mapping(dto)

        return if(dto.id > 0){
            db?.update(PaidDB.Entry.TABLE_NAME,content,"${BaseColumns._ID}=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(PaidDB.Entry.TABLE_NAME, null, content) ?: 0
        }.also {
            Log.d(javaClass.name,"=== Save ${dto.id} ${dto.date} ${dto.recurrent} ${dto.end} Response $it")
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
            PaidDB.Entry.TABLE_NAME,COLUMNS,"_id = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = PaidMap()
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(mapper.mapping(cursor))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPaid(date:LocalDate):PaidDTO{
        val id = 0
        val account = ""
        val name = ""
        val value = BigDecimal.ZERO
        val recurrent = 0
        val endDate = LocalDate.of(9999,12,31)
        return PaidDTO(id,date, account,name,value,recurrent.toShort(),endDate)
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
    override fun getPeriods(codeAccount: Long):List<PaidDTO>{
        val db = dbConnect.readableDatabase
        val cursor = db.rawQuery(
            "select max(${PaidDB.Entry.COLUMN_DATE_PAID}) from ${PaidDB.Entry.TABLE_NAME} WHERE ${PaidDB.Entry.COLUMN_ACCOUNT} like ?"
            ,arrayOf("%${codeAccount}%")
        )
        val list = mutableListOf<PaidDTO>()
        with(cursor){
            while(moveToNext()){
                val datePaid = cursor.getString(0)
                datePaid?.let{
               var date = DateUtils.toLocalDate(datePaid)
                date = date.withDayOfMonth(1)
                val periods = Period.between(date,LocalDate.now())
                for( i in 0..periods.months) {
                    val dateFound = date.plusMonths(i.toLong())
                    val records = get(getPaid(dateFound)).toMutableList()
                    getRecurrent(dateFound)?.let {
                        if (it.isNotEmpty()) {
                            records.addAll(it)
                        }
                    }
                    val value = if (records.isNotEmpty()) records.sumOf { it.value } else BigDecimal.ZERO
                    list.add(getPeriodPaidDTO(dateFound, value,records.count()))
                }
                }
            }
        }
        Log.d(javaClass.name,"Periods Records ${list.size}")
        return list.sortedByDescending { it.date }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPeriodPaidDTO(date:LocalDate, value:BigDecimal,count:Int):PaidDTO{
        val id = count
        val account = ""
        val name = ""
        val recurrent = 0
        val endDate = LocalDate.of(9999,12,31)
        return PaidDTO(id,date,account,name,value,recurrent.toShort(),endDate)
    }


    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getValues(date: LocalDate): List<GraphValuesResp> {
        val list = get(getPaid(date))
        val recurrents = getRecurrent(date)
        val response =list.map{
            GraphValuesResp(it.id.toLong(),it.name,it.value.toDouble())
        }.toMutableList()
        response.add(GraphValuesResp(0,"Recurrent",recurrents.sumOf { it.value }.toDouble()))
        return response
    }
}