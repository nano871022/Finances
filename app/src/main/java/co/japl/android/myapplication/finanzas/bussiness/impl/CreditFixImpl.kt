package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.AdditionalMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.CreditMap
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import co.japl.android.myapplication.utils.DatabaseConstants
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

class CreditFixImpl(override var dbConnect: SQLiteOpenHelper) :SaveSvc<CreditDTO> , ICreditFix{
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
            db?.update(CreditDB.Entry.TABLE_NAME,content,"id=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
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


}