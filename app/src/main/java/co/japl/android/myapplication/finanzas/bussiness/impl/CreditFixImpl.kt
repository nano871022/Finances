package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.*

class CreditFixImpl(override var dbConnect: SQLiteOpenHelper) :ICreditFix{
    private val additionalSvc:SaveSvc<AdditionalCreditDTO> = AdditionalCreditImpl(dbConnect)
    private val calcTaxSvc = KindOfTaxImpl()
    private val calc = QuoteCredit()
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        CreditDB.Entry.COLUMN_KIND_OF,
        CreditDB.Entry.COLUMN_NAME,
        CreditDB.Entry.COLUMN_VALUE,
        CreditDB.Entry.COLUMN_PERIODS,
        CreditDB.Entry.COLUMN_TAX,
        CreditDB.Entry.COLUMN_QUOTE,
        CreditDB.Entry.COLUMN_DATE,
        CreditDB.Entry.COLUMN_KIND_OF_TAX
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = CreditMap().mapping(dto)
        return if(dto.id > 0){
            db?.update(CreditDB.Entry.TABLE_NAME,content,"_id=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(CreditDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CreditDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(CreditDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<CreditDTO>()
        val mapper = CreditMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(CreditDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CreditDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS,"id = ?",
            arrayOf(id.toString()),null,null,null)
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(CreditMap().mapping(cursor))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: CreditDTO): List<CreditDTO> {
        val db = dbConnect.readableDatabase
        val list = mutableListOf<CreditDTO>()
        val cursor = db.query(CreditDB.Entry.TABLE_NAME,COLUMNS,null,
            null,null,null,null)
        with(cursor){
            while(moveToNext()){
                val value = CreditMap().mapping(cursor)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestAll(date:LocalDate): BigDecimal {
        return getAll().map {
            val periodPaid =  ChronoUnit.MONTHS.between(date,it.date).toInt()
            calc.getInterest(it.value,it.tax,it.periods,it.quoteValue,periodPaid,
                KindOfTaxEnum.valueOf(it.kindOfTax))
        }.reduceOrNull{acc,bigDecimal->acc+bigDecimal} ?:BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalAll(date:LocalDate): BigDecimal {
        return getAll().map {
            val periodPaid =  ChronoUnit.MONTHS.between(date,it.date).toInt()
            it.quoteValue - calc.getInterest(it.value,it.tax,it.periods,it.quoteValue,periodPaid,
                KindOfTaxEnum.valueOf(it.kindOfTax))
        }.reduceOrNull{acc,bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getQuoteAll(): BigDecimal {
        return getAll().map { it.quoteValue + getAdditional(it.id.toLong()) }
            .reduceOrNull{ acc, bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditional(id:Long):BigDecimal{
        return (additionalSvc as ISaveSvc<AdditionalCreditDTO>).get(createAdditional(id))
            .map { it.value }
            .reduceOrNull { acc, bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAdditional(id:Long):AdditionalCreditDTO{
        return AdditionalCreditDTO(0,"",
            BigDecimal.ZERO,id,
            LocalDate.MIN,
            LocalDate.MAX)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPayAll(date:LocalDate): BigDecimal {
        return getAll().map {
            val periodPaid = ChronoUnit.MONTHS.between(it.date,date).toInt()
            it.value - calc.getCreditValue(it.value,it.tax,periodPaid,it.periods,it.quoteValue, KindOfTaxEnum.valueOf(it.kindOfTax))
        }.reduceOrNull{ acc, bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAdditionalAll(): BigDecimal {
        return additionalSvc.getAll().filter { it.endDate > LocalDate.now() }.map { it.value }
            .reduceOrNull { acc, bigDecimal ->  acc +  bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuoteValue(date:LocalDate):Pair<Int,BigDecimal>{
        val list = getAll()
            .filter { (it.date > date && it.date < date.plusMonths(1).minusDays(1)) || it.date < date }.toMutableList()
        val quote =  list
            .map { it.quoteValue }
            .reduceOrNull(){acc,value->acc + value} ?: BigDecimal.ZERO
        val additional = list.map { additionalSvc.get(it.id) }
            .filter { it.isPresent }
            .map { it.get().value }
            .reduceOrNull{acc,value->acc+value} ?: BigDecimal.ZERO
        return Pair(list.size,quote + additional)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPeriods(): List<PeriodCreditDTO> {
        val list = mutableListOf<PeriodCreditDTO>()
        val db = dbConnect.writableDatabase
        val cursor = db.rawQuery("SELECT min(${CreditDB.Entry.COLUMN_DATE}) FROM ${CreditDB.Entry.TABLE_NAME}",null)
        with(cursor){
            while (moveToNext()){
                val date = DateUtils.toLocalDate(cursor.getString(0))
                val periods = Period.between(date,LocalDate.now())
                Log.d(javaClass.name,"Periods: ${periods.toTotalMonths()} $periods $date ${LocalDate.now()}")
                for(i in 0..periods.toTotalMonths()) {
                    val date = date.plusMonths(i.toLong()).withDayOfMonth(1)
                    val value = getQuoteValue(date)
                    list.add(PeriodCreditDTO(date, value.first, value.second))
                }
            }
        }
        return list.sortedByDescending { it.date }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTotalQuote(): BigDecimal {
        val list = getAll().filter { LocalDate.now() < it.date.plusMonths(it.periods.toLong()) }
        val additionals = getAdditionalAll()
        val quote =  list.sumOf { it.quoteValue }
        return quote + additionals
    }


}