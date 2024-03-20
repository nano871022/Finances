package co.japl.android.finances.services.dao.implement

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.implement.CreditCardImpl
import co.japl.android.finances.services.implement.DifferInstallmentImpl
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.interfaces.SaveSvc
import co.japl.android.finances.services.mapping.CreditCardBoughtMap
import co.japl.android.finances.services.mapping.CreditCardMap
import co.japl.android.finances.services.utils.DatabaseConstants
import co.japl.android.finances.services.utils.DateUtils
import com.google.gson.Gson
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.util.*
import javax.inject.Inject

class SaveCreditCardBoughtImpl @Inject constructor(val context:Context, override var dbConnect: SQLiteOpenHelper) :
    IQuoteCreditCardDAO {
    private val COLUMNS_CALC = arrayOf(BaseColumns._ID
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX
    ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE)

    private val creditCardSvc:SaveSvc<CreditCardDTO> = CreditCardImpl(dbConnect)
    private val differInstallmentSvc = DifferInstallmentImpl(dbConnect)
    private val FORMAT_DATE_BOUGHT_WHERE = "substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},7,4)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},4,2)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},1,2)"
    private val FORMAT_DATE_END_WHERE = "substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE},7,4)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE},4,2)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE},1,2)"
    private val FORMAT_DATE_BOUGHT_WHERE_YYYYMM = "substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},7,4)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},4,2)"
    private val FORMAT_DATE_BOUGHT_WHERE_MMYYYY = "substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},1,4)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},6,2)"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardBoughtDTO): Long {
        Log.v(this.javaClass.name,"<<<=== save - Start")
        if(dto.endDate == LocalDateTime.MAX){
            dto.endDate = LocalDateTime.of(LocalDate.of(9999,12,31),LocalTime.MAX)
        }
            val db = dbConnect.writableDatabase
            val values = CreditCardBoughtMap().mapping(dto)
            return if(dto.id > 0){
                (db?.update(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,values,"${BaseColumns._ID}=?",
                    arrayOf(dto.id.toString())
                )?.toLong()?:0).also { Log.v(this.javaClass.name, "<<<=== END:Save $values $it") }
            }else {
                 (db?.insert(
                    CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
                    null,
                    values
                )!!).also { Log.v(this.javaClass.name, "<<<=== END:Save $values $it") }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPeriods(creditCardId:Int):List<PeriodDTO>{
        Log.d(this.javaClass.name,"<<<=== getPeriods - Start")
        return arrayListOf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override  fun getAll():List<CreditCardBoughtDTO>{
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
            val db = dbConnect.readableDatabase

            val cursor = db.query(
                CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
                COLUMNS_CALC,
                null,
                null,
                null,
                null,
                null
            )
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor) {
                while (moveToNext()) {
                    items.add(CreditCardBoughtMap().mapping(this))
                }
            }

            return items.also { Log.v(this.javaClass.name,"<<<=== getAll - End ${it.size}") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun delete(id:Int):Boolean{
        Log.v(this.javaClass.name,"<<<=== delete - Start")
        differInstallmentSvc.get(id)
            ?.takeIf { it.isPresent }
            ?.let { differ->
                val differ = get(id)
                getDifferOriginBought(differ.get().nameItem,differ.get().boughtDate,differ.get().endDate)
                    ?.let {bought->
                        bought.endDate = LocalDateTime.of(9999,12,31,11,59,59)
                        bought.id = 0
                        save(bought)
                    }
                differInstallmentSvc.delete(differ.get().id)
            }

        val db = dbConnect.writableDatabase
        return (db.delete(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
            DatabaseConstants.SQL_DELETE_CALC_ID, arrayOf(id.toString())) > 0).also { Log.v(this.javaClass.name,"<<<=== delete - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDifferOriginBought(name:String,bougthDate:LocalDateTime,endDate:LocalDateTime):CreditCardBoughtDTO?{
        val db = dbConnect.writableDatabase
        val boughtStr = DateUtils.localDateTimeToStringDate(bougthDate)
        val endStr = DateUtils.localDateTimeToStringDate(endDate)
        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
            COLUMNS_CALC,
            """
                ($FORMAT_DATE_BOUGHT_WHERE = ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} = ?)
                AND ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM} = ?
                AND ($FORMAT_DATE_END_WHERE <= ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE} <= ?) 
             """,
            arrayOf(boughtStr,boughtStr,name,endStr,endStr),null,null,"${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE} DESC")
        with(cursor) {
            if (moveToNext()) {
                return CreditCardBoughtMap().mapping(this).also { Log.d(javaClass.name,"<<<=== END:getDifferOriginBought $it") }
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getToDate(key:Int,startDate:LocalDateTime,cutOffDate: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.d(this.javaClass.name,"<<<=== STARTING::getToDate")
            val db = dbConnect.readableDatabase
            val startDateStr = DateUtils.localDateTimeToStringDate(startDate)
            val endDateStr = DateUtils.localDateTimeToStringDate(cutOffDate)
            val cursor = db.query(
                CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
                COLUMNS_CALC,
                """ 
                     ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ?
                    AND (date($FORMAT_DATE_BOUGHT_WHERE) between ? and ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} between ? and ?)
                    AND (date($FORMAT_DATE_END_WHERE) > ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE} > ?)
                    AND ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT} = 0
                """.trimMargin(),
                arrayOf(key.toString(),startDateStr, endDateStr,startDateStr, endDateStr,startDateStr,startDateStr),
                null,
                null,
                "${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} ASC"
            )
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor) {
                while (moveToNext()) {
                    CreditCardBoughtMap().mapping(this)?.let {
                        if (it.endDate > startDate) {
                            items.add(it)
                        }
                    }
                }
            }
            return items.also { Log.d(this.javaClass.name,"<<<=== ENDING::getToDate Size:${it.size}") }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingQuotes(key:Int,startDate: LocalDateTime,cutoffCurrent: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.d(this.javaClass.name,"<<<=== START::getPendingQuotes Key: $key Start: $startDate Cutoff: $cutoffCurrent")
        val startDateStr = DateUtils.localDateTimeToStringDate(startDate)
        val initDateStr = DateUtils.localDateTimeToStringDate(LocalDateTime.of(1900,1,1,0,0,0))
        val db = dbConnect.readableDatabase
        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_CALC,
            """
                        ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH} > 1 
                    and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ?
                    and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT} = 0 
                    and ($FORMAT_DATE_BOUGHT_WHERE between ? and ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} between ? and ?)
                    and ($FORMAT_DATE_END_WHERE >= ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE} >= ?)
                    """,
            arrayOf(key.toString(),initDateStr,startDateStr,initDateStr,startDateStr,startDateStr,startDateStr),null,null,null)

        val items = mutableListOf<CreditCardBoughtDTO>()
        with(cursor){
            while(moveToNext()){
                CreditCardBoughtMap().mapping(this)
                    ?.takeIf { it.endDate >= startDate }
                    ?.takeIf { it.boughtDate <= cutoffCurrent }
                    ?.takeIf {getValidMonths(it,cutoffCurrent,startDate)}
                    ?.let{items.add(it)}
            }
        }
        return items.also { Log.d(this.javaClass.name,"<<<=== FINISH::getPendingQuotes Size: ${it.size}") }
    }

    override fun getCapital(
        key: Int,
        startDate: LocalDateTime,
        cutOff: LocalDateTime
    ): Optional<BigDecimal> {
        TODO("Not yet implemented")
    }

    override fun getInterest(
        key: Int,
        startDate: LocalDateTime,
        cutOff: LocalDateTime
    ): Optional<BigDecimal> {
        TODO("Not yet implemented")
    }

    override fun getCapitalPendingQuotes(
        key: Int,
        startDate: LocalDateTime,
        cutOff: LocalDateTime
    ): Optional<BigDecimal> {
        TODO("Not yet implemented")
    }

    override fun getInterestPendingQuotes(
        key: Int,
        startDate: LocalDateTime,
        cutOff: LocalDateTime
    ): Optional<BigDecimal> {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getValidMonths(creditCardBoughtDTO:CreditCardBoughtDTO,cutoffCurrent: LocalDateTime,startCutOff:LocalDateTime):Boolean{
        val differInstallment = differInstallmentSvc.get(creditCardBoughtDTO.id)
        return (differInstallment.takeIf { it.isPresent }?.let {
            return (DateUtils.getMonths(LocalDateTime.of(it.get().create, LocalTime.MAX),cutoffCurrent) < it.get().newInstallment).also {
                Log.d(javaClass.name,"validMonths: End: ${creditCardBoughtDTO.endDate} CutOff: $cutoffCurrent Bought: ${creditCardBoughtDTO.boughtDate} Name: ${creditCardBoughtDTO.nameItem} It: $it")
            }
        } ?: (DateUtils.getMonths(creditCardBoughtDTO.boughtDate,cutoffCurrent) < creditCardBoughtDTO.month)
                ).also {
            Log.d(javaClass.name,"validMonths: End: ${creditCardBoughtDTO.endDate} CutOff: $cutoffCurrent Bought: ${creditCardBoughtDTO.boughtDate} Name: ${creditCardBoughtDTO.nameItem} Create: ${creditCardBoughtDTO.createDate} It: $it")
        }
    }




    /**
     * QUOTES PENDING MORE THAN ONE QUOTE TO PAID
     */

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBought(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<Long> {
        Log.d(this.javaClass.name,"<<<=== getBought - Start")
            val list = getToDate(key, startDate,cutOff)
            val listRecurrent = getRecurrentBuys(key,cutOff)
            val countRecurrent = listRecurrent.count { it.month == 1}
            val value = list.count { it.month <= 1 }
            return Optional.ofNullable((value + countRecurrent).toLong()).also {
                Log.d(this.javaClass.name,"<<<=== getBought - End $it")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBoughtPendingQuotes(key: Int, startDate: LocalDateTime,cutOff: LocalDateTime): Optional<Long> {
        Log.d(this.javaClass.name,"<<<=== getBoughtPendingQuotes - Start")
            val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())
            val list = getPendingQuotes(key, startDate,cutOff)
            val value = list.stream().filter {
                differQuotes.firstOrNull{differ->differ.cdBoughtCreditCard.toInt() == it.id}?.let{
                    DateUtils.getMonths(LocalDateTime.of(it.create,LocalTime.MAX),cutOff) <= it.newInstallment
                }?:(DateUtils.getMonths(it.boughtDate,cutOff) <= it.month)
            }.count()
            return Optional.ofNullable(value).also{Log.d(this.javaClass.name,"<<<=== getBoughtPendingQuotes - End $it")}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBoughtQuotes(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<Long> {
        Log.d(this.javaClass.name,"<<<=== getBoughtQuotes - Start")
            val list = getToDate(key,startDate,cutOff)
            val listRecurrent = getRecurrentBuys(key,cutOff).toMutableList()
            getRecurrentPendingQuotes(key, cutOff)?.let { listRecurrent.addAll(it) }
            val countRecurrent = listRecurrent.count{ it.month > 1}
            val value =  list.count{ it.month > 1 }
            return Optional.ofNullable((value+countRecurrent).toLong()).also { Log.d(this.javaClass.name,"<<<=== getBoughtQuotes - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPay(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== getPendingToPay - Start")
            val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())
            val list = getToDate(key,startDate,cutOff)
            val value = list.stream().map {
                val differ = differQuotes.firstOrNull{differ->differ.cdBoughtCreditCard.toInt() == it.id}
                val months = differ?.let{DateUtils.getMonths(it.create,cutOff)}?:DateUtils.getMonths(it.boughtDate,cutOff)
                val quote = differ?.let{(it.pendingValuePayable / it.newInstallment).toBigDecimal()}?:it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
                val bought = quote.multiply(months.toBigDecimal())
                val capital = differ?.let{it.pendingValuePayable.toBigDecimal().minus(bought)}?:it.valueItem.minus(bought)
                if(capital > BigDecimal.ZERO && it.month > 1){
                    capital
                }else{
                    BigDecimal.ZERO
                }
            }.reduce{val1,val2->val1.plus(val2)}
            return value.also { Log.d(this.javaClass.name,"<<<=== getPendingToPay - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPayQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== getPendingToPayQuotes - Start")
            val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())
            val list = getPendingQuotes(key,startDate,cutOff)
            val value = list.stream().map {
                val differ = differQuotes.firstOrNull{differ->differ.cdBoughtCreditCard.toInt() == it.id}
                val months =  differ?.let{DateUtils.getMonths(it.create,cutOff)}?:DateUtils.getMonths(it.boughtDate,cutOff)
                val quote = differ?.let{(it.pendingValuePayable / it.newInstallment).toBigDecimal()}?: it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
                val bought = quote.multiply(months.toBigDecimal())
                val capital = differ?.let{it.pendingValuePayable.toBigDecimal().minus(bought)}?: it.valueItem.minus(bought)
                if(capital > BigDecimal.ZERO){
                    capital
                }else{
                    BigDecimal.ZERO
                }
            }.reduce{val1,val2->val1.plus(val2)}
            return value.also { Log.d(this.javaClass.name,"<<<=== getPendingToPayQuotes - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CreditCardBoughtDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
            COLUMNS_CALC,
            " ${BaseColumns._ID}= ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                return Optional.ofNullable(CreditCardBoughtMap().mapping(this))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getRecurrentBuys(key: Int, cutOff: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.d(this.javaClass.name,"<<<=== STARTING::getRecurrentBuys key: $key CutOff: $cutOff")
            val cutOffDay = creditCardSvc.get(key).get().cutOffDay
            val db = dbConnect.readableDatabase
            val cutOffLastMonth = DateUtils.cutOffLastMonth(cutOffDay,cutOff)
            val startCutOffLastMonth = DateUtils.startDateFromCutoff(cutOffDay,cutOffLastMonth)

            val endDateStr = DateUtils.localDateTimeToStringDate(cutOffLastMonth)
            val startDateStr = DateUtils.localDateTimeToStringDate(startCutOffLastMonth)

            val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_CALC,
                """
                    ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT} = ? 
                    and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ?
                    and ($FORMAT_DATE_BOUGHT_WHERE <= ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} <= ?)
                    and ($FORMAT_DATE_END_WHERE >= ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE} >= ?)
                """.trimMargin(),
                arrayOf("1",key.toString(),startDateStr,startDateStr,endDateStr,endDateStr),null,null,null)
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor){
                while(moveToNext()){
                     CreditCardBoughtMap().mapping(this)?.let {
                         if (it.endDate >= cutOffLastMonth ){
                             items.add(it)
                     }
                     }
                }
            }
            return items.also { Log.d(this.javaClass.name,"<<<=== ENDING::getRecurrentBuys StartDate: $startDateStr EndDate: $endDateStr Size: ${it.size}") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>{
        Log.d(javaClass.name,"<<<=== STARTING::getRecurrentPendingQuotes Code CreditCard: $key CutOff: $cutOff")
        val db = dbConnect.readableDatabase
        val cutOffDay = creditCardSvc.get(key).get().cutOffDay
        val cutOffLastMonth = DateUtils.cutOffLastMonth(cutOffDay,cutOff)
        val cutOffLastMonthStr = DateUtils.localDateTimeToStringDate(cutOffLastMonth)

        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_CALC,
        """
            ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ?
            and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT} = 1
            and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH} > 1
            and ($FORMAT_DATE_BOUGHT_WHERE <= ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} <= ?)
        """.trimIndent(), arrayOf(key.toString(),cutOffLastMonthStr,cutOffLastMonthStr),null,null,null)
        val items = mutableListOf<CreditCardBoughtDTO>()
        with(cursor){
            while (moveToNext()){
                CreditCardBoughtMap().mapping(this)?.let{items.add(it)}
            }
        }
        var status = true
        var cutOffNew = cutOffLastMonth
        val itemsTemp = mutableListOf<CreditCardBoughtDTO>()
        var i = itemsTemp.size
        while(status){
            items.forEach{
                val copy = it.copy()
                copy.boughtDate = it.boughtDate.withMonth(cutOffNew.monthValue).withYear(cutOffNew.year)
                val startCutOff = DateUtils.startDateFromCutoff(cutOffDay,cutOffNew)
                if(DateUtils.getMonths(copy.boughtDate,cutOff) < it.month && copy.endDate.isAfter(startCutOff)){
                    itemsTemp.add(copy)
                }
            }
            cutOffNew = DateUtils.cutOffLastMonth(cutOffDay,cutOffNew)
            if(i < itemsTemp.size){
                i = itemsTemp.size
            }else{
                status = false
            }
        }
        return itemsTemp.also { Log.d(javaClass.name,"<<<=== ENDING::getRecurrentPendingQuotes Size: ${it.size}") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun endingRecurrentPayment(idBought: Int, cutOff:LocalDateTime): Boolean {
        Log.d(javaClass.name,"<<<=== STARTING::endingRecurrentPayment Id: $idBought CutOff: $cutOff")
        val bought = get(idBought)
        if(bought.isPresent){
            val creditCard = creditCardSvc.get(bought.get().codeCreditCard)
            if(creditCard.isPresent) {
                val endDate = DateUtils.startDateFromCutoff(creditCard.get().cutOffDay, cutOff).minusDays(2)
                bought.get().endDate = endDate
                return (save(bought.get()) > 0).also { Log.d(javaClass.name,"<<<=== ENDING::endingRecurrentPayment Id: $idBought cutOff: $cutOff EndDate: $endDate Response: $it") }
            }
        }
        return false.also { Log.d(javaClass.name,"<<<=== ENDING::endingRecurrentPayment BAD Validation Id: $idBought cutOff: $cutOff Response: $it")  }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun backup(pathFile: String) {
        val values = getAll()
        val path = Paths.get(pathFile)
        Files.newBufferedWriter(path, Charset.defaultCharset()).use { it.write(Gson().toJson(values)) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun restoreBackup(pathFile: String) {
        val path = Paths.get(pathFile)
        val list = Files.newBufferedReader(path, Charset.defaultCharset()).use { Gson().fromJson(it,List::class.java) } as List<CreditCardBoughtDTO>
        list.forEach(this::save)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun getLastAvailableQuotesTC(): List<QuoteCreditCard> {
        val creditCards = creditCardSvc.getAll().filter { it.status }
        val list = arrayListOf <QuoteCreditCard>()
        for ( creditCard in creditCards) {
            val creditCardPojo = CreditCardMap().mapper(creditCard)
            val endDate = DateUtils.cutOffLastMonth(creditCardPojo.cutoffDay.get())
            val startDate = DateUtils.startDateFromCutoff(creditCardPojo.cutoffDay.get(),endDate)
            val capital = getCapital(creditCard.id, startDate, endDate)
            val capitalQuotes =
                getCapitalPendingQuotes(creditCard.id, startDate, endDate)
            val interest = getInterest(creditCard.id, startDate, endDate)
            val interestQuote =
                getInterestPendingQuotes(creditCard.id, startDate, endDate)
            val dto = QuoteCreditCard()
            dto.creditCardId = Optional.of(creditCard.id)
            dto.name = Optional.of(creditCard.name)
            dto.capitalValue = Optional.of(capital.orElse(BigDecimal.ZERO) + capitalQuotes.orElse(BigDecimal.ZERO))
            dto.interestValue = Optional.of(interestQuote.orElse(BigDecimal.ZERO) + interest.orElse(BigDecimal.ZERO))
            list.add(dto)
        }
        return list

    }

    override fun getPeriod(creditCardId: Int): List<YearMonth> {
        Log.i(this.javaClass.name,"<<<=== START Get Periods")
        val db = dbConnect.readableDatabase
        val cursor = db.rawQuery("""
            SELECT DISTINCT $FORMAT_DATE_BOUGHT_WHERE_YYYYMM , $FORMAT_DATE_BOUGHT_WHERE_MMYYYY
            FROM ${CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME}
            WHERE ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ?
        """
            ,
            arrayOf(creditCardId.toString()))
        val items = mutableListOf<YearMonth>()
        with(cursor){
            while(moveToNext()) {
                    getStringOrNull(0)?.let {items(it,items::add)}
                    getStringOrNull(1)?.let {items(it,items::add)}
            }
        }

        return items.distinct().also { Log.i(this.javaClass.name,"<<<=== FINISH Get all records Count ${it.size}") }
    }

    private fun items(value:String,exec:(period:YearMonth)->Unit){
        try{
        value.split("-").let {
            if(it[0].length == 4) {
                exec.invoke(YearMonth.of(it[0].toInt(), it[1].toInt()))
            }
        }
        }catch(e:DateTimeException){}
        catch (e:NumberFormatException){}

    }

   override fun findByNameAndBoughtDateAndValue(name:String,boughtDate:LocalDateTime,amount:BigDecimal):CreditCardBoughtDTO? {
        return getAll().takeIf { it.isNotEmpty() }?.filter {
            it.nameItem.trim().contains(name.replace("(SMS*)","").trim())
                    && it.boughtDate.isEqual(boughtDate)
                    && it.valueItem.toDouble() == amount.toDouble()
        }?.firstOrNull()
    }

}