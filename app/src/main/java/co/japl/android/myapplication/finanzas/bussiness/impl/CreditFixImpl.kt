package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGracePeriod
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
import javax.inject.Inject

class CreditFixImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) :ICreditFix{
    private val additionalSvc:IAdditionalCreditSvc = AdditionalCreditImpl(dbConnect)
    private val gracePeriodSvc:IGracePeriod = GracePeriodImpl(dbConnect)
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
            db?.update(CreditDB.Entry.TABLE_NAME,content,"${BaseColumns._ID}=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
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
        val cursor = db.query(CreditDB.Entry.TABLE_NAME,COLUMNS,"${BaseColumns._ID} = ?",
            arrayOf(id.toString()),null,null,null)
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(CreditMap().mapping(cursor))
            }
        }
        return Optional.empty()
    }
    private val FORMAT_DATE_BOUGHT_WHERE = "substr(${CreditDB.Entry.COLUMN_DATE},7,4)||'-'||substr(${CreditDB.Entry.COLUMN_DATE},4,2)||'-'||substr(${CreditDB.Entry.COLUMN_DATE},1,2)"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: CreditDTO): List<CreditDTO> {
        val db = dbConnect.readableDatabase
        val list = mutableListOf<CreditDTO>()
        val dateFormatter = DateUtils.localDateToStringDate(values.date)
        val cursor = db.query(CreditDB.Entry.TABLE_NAME,COLUMNS
            ,"$FORMAT_DATE_BOUGHT_WHERE < ?",
            arrayOf(dateFormatter),null,null,null)
        with(cursor){
            while(moveToNext()){
                val value = CreditMap().mapping(cursor)
                if(gracePeriodSvc.get(value.id,values.date).isPresent){
                    value.quoteValue = BigDecimal.ZERO
                }
                if(value.date.plusMonths(value.periods.toLong()).isAfter(values.date)) {
                    list.add(value)
                }
            }
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCurrentBoughtCredits(date:LocalDate):List<CreditDTO>{
        val db = dbConnect.readableDatabase
        val list = mutableListOf<CreditDTO>()
        val dateFormatter = DateUtils.localDateToStringDate(date)
        val cursor = db.query(CreditDB.Entry.TABLE_NAME,COLUMNS
            ,"$FORMAT_DATE_BOUGHT_WHERE < ?",
            arrayOf(dateFormatter),null,null,null)
        with(cursor){
            while(moveToNext()){
                val value = CreditMap().mapping(cursor)
                if(gracePeriodSvc.get(value.id,date).isPresent){
                    value.quoteValue = BigDecimal.ZERO
                }
                list.add(value)
            }
        }
        return list
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }

    private fun getCredit(date:LocalDate):CreditDTO{
        val id = 0
        val name = ""
        val tax = 0.0
        val period = 0
        val value = BigDecimal.ZERO
        val quote = BigDecimal.ZERO
        val kindOf = ""
        val kindOfTax = ""
        return CreditDTO(id, name ,date,tax,period,value,quote,kindOf,kindOfTax)
    }

    private fun getCountGracePeriods(id:Int):Int{
        return gracePeriodSvc.get(id.toLong()).takeIf { it.isNotEmpty() }?.map {it.periods.toInt()}?.reduce { acc, sh -> acc+ sh } ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestAll(date:LocalDate): BigDecimal {
        return get(getCredit(date)).filter {
            !gracePeriodSvc.get(it.id,LocalDate.now()).isPresent
        }.map {
            val gracePeriods = getCountGracePeriods(it.id)
            val periodPaid = ChronoUnit.MONTHS.between(date, it.date).toInt() - gracePeriods
            calc.getInterest(
                    it.value, it.tax, it.periods, it.quoteValue, periodPaid,
                    KindOfTaxEnum.valueOf(it.kindOfTax)
                )
        }.reduceOrNull{acc,bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalAll(date:LocalDate): BigDecimal {
        return get(getCredit(date)).filter {
            !gracePeriodSvc.get(it.id,LocalDate.now()).isPresent
        }.map {
            val gracePeriods = getCountGracePeriods(it.id)
            val periodPaid =  ChronoUnit.MONTHS.between(date,it.date).toInt() - gracePeriods
            it.quoteValue - calc.getInterest(it.value,it.tax,it.periods,it.quoteValue,periodPaid,
                KindOfTaxEnum.valueOf(it.kindOfTax))
        }.reduceOrNull{acc,bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getQuoteAll(date: LocalDate): BigDecimal {
        return get(getCredit(date)).filter {
            !gracePeriodSvc.get(it.id,LocalDate.now()).isPresent
        }.map { it.quoteValue + getAdditional(it.id.toLong()) }
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
        return get(getCredit(date)).map {
            val gracePeriods = getCountGracePeriods(it.id)
            val periodPaid = ChronoUnit.MONTHS.between(it.date,date).toInt() - gracePeriods
            it.value - calc.getCreditValue(it.value,it.tax,periodPaid,it.periods,it.quoteValue, KindOfTaxEnum.valueOf(it.kindOfTax))
        }.reduceOrNull{ acc, bigDecimal->acc+bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAdditionalAll(date:LocalDate): BigDecimal {
        return additionalSvc.getAll().filter { it.endDate > date }.map { it.value }
            .reduceOrNull { acc, bigDecimal ->  acc +  bigDecimal} ?: BigDecimal.ZERO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuoteValue(date:LocalDate):Pair<Int,BigDecimal>{
        val list = get(getCredit(date))
            .filter { (it.date > date && it.date < date.plusMonths(1).minusDays(1)) || it.date < date }
            .filter{!gracePeriodSvc.get(it.id,date).isPresent}.toMutableList()
        val quote =  list
            ?.sumOf { it.quoteValue }
        val additional = list.map { additionalSvc.get(it.id.toLong()) }
            ?.flatMap { it.toList() }
            ?.sumOf { it.value }
        return Pair(list.size,(quote  ?: BigDecimal.ZERO ) + (additional ?: BigDecimal.ZERO))
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
    override fun getTotalQuote(date: LocalDate): BigDecimal {
        val list = getAll().filter { date < it.date.plusMonths(it.periods.toLong()) }
        val additionals = list.map {
            additionalSvc.get(it.id.toLong())
        }?.flatMap { it.toList() }
            ?.sumOf { it.value }

        val quote =  list.sumOf { it.quoteValue }
        return quote + (additionals ?: BigDecimal.ZERO)
    }


}