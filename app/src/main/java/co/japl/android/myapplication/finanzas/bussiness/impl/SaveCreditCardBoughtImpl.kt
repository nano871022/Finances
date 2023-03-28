package co.japl.android.myapplication.bussiness.impl

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.*
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.bussiness.mapping.CreditCardBoughtMap
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGetPeriodsServices
import co.japl.android.myapplication.finanzas.bussiness.mapping.PeriodsMap
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import com.google.gson.Gson
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class SaveCreditCardBoughtImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<CreditCardBoughtDTO>,
    SearchSvc<CreditCardBoughtDTO>, IGetPeriodsServices {
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
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_KIND_OF_TAX)

    private val COLUMNS_PERIOD = arrayOf(BaseColumns._ID,
        CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH
    )
    private val creditCardSvc:SaveSvc<CreditCardDTO> = CreditCardImpl(dbConnect)
    private val taxSvc = TaxImpl(dbConnect)
    private val buyCCSettingSvc = BuyCreditCardSettingImpl(dbConnect)
    private val creditCardSettingSvc = CreditCardSettingImpl(dbConnect)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardBoughtDTO): Long {
        Log.v(this.javaClass.name,"<<<=== save - Start")
        try{
        val db = dbConnect.writableDatabase
        val dto = CreditCardBoughtMap().mapping(dto)
        return (db?.insert(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,null,dto)!! ).also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== save - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPeriods(creditCardId:Int):List<PeriodDTO>{
        Log.i(this.javaClass.name,"<<<=== START Get Periods")
        val db = dbConnect.readableDatabase
        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_PERIOD,"str_code_credit_card = ?",
            arrayOf(creditCardId.toString()),null,null,null)
        val creditCardDto = creditCardSvc.get(creditCardId)
        val items = mutableListOf<PeriodDTO>()
        with(cursor){
            while(moveToNext()) {
                val map = PeriodsMap(taxSvc,buyCCSettingSvc,creditCardSettingSvc).maping(this,creditCardDto.get().cutOffDay.toInt())
                if(map.isPresent) {
                    Log.d(
                        this.javaClass.name,
                        "ID $creditCardId Capital: ${map.get().capital} Interest: ${map.get().interest} Total: ${map.get().total} CutOff: ${map.get().periodEnd}"
                    )
                    items.add(map.get())
                }
            }}
            val responseList = items.groupBy { it.periodStart }.map { (startPeriod,items) -> PeriodDTO(creditCardId,startPeriod,
                items[0].periodEnd,items.sumOf{it.interest},items.sumOf{it.capital},items.sumOf{it.total})}
                .sortedByDescending { it.periodStart }


            responseList.forEach { map ->
                    val list = getRecurrentBuys(creditCardId, map.periodEnd)
                    val capitalRecurrent = list.stream().map { it.valueItem.divide(it.month.toBigDecimal(),RoundingMode.CEILING) }
                        .reduce { accumulator, bought -> accumulator.add(bought) }
                    val interestRecurrent = list.stream().filter{it.month > 1}.map {
                        it.valueItem.multiply(it.interest.toBigDecimal().divide(BigDecimal(100)))
                    }
                        .reduce{ accumulator , bought -> accumulator.add(bought)}
                    map.capital = map.capital.add(capitalRecurrent.orElse(BigDecimal.ZERO))
                    map.interest = map.interest.add(interestRecurrent.orElse(BigDecimal.ZERO))
                    map.total = map.capital.add(map.interest)
                    Log.d(
                        this.javaClass.name,
                        "Recurrent: Capital: $capitalRecurrent Interes: $interestRecurrent Capital ${map.capital} Interest: ${map.interest} Total: ${map.total}"
                    )
                    val capitalPending =
                        getCapitalPendingQuotes(creditCardId, map.periodStart, map.periodEnd)
                    val interestPending =
                        getInterestPendingQuotes(creditCardId, map.periodStart, map.periodEnd)
                    map.capital = map.capital.add(capitalPending.orElse(BigDecimal.ZERO))
                    map.interest = map.interest.add(interestPending.orElse(BigDecimal.ZERO))
                    map.total = map.capital.add(map.interest)
                    Log.v(
                        this.javaClass.name,
                        "Pending Capital: $capitalPending Interest: $interestPending ${map.creditCardId} ${map.capital} ${map.interest} ${map.total}"
                    )
            }
        Log.i(this.javaClass.name,"<<<=== FINISH Get Periods Count ${items.size}")
        return responseList
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

    override fun delete(id:Int):Boolean{
        Log.v(this.javaClass.name,"<<<=== delete - Start")
        val db = dbConnect.writableDatabase
        return (db.delete(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
            DatabaseConstants.SQL_DELETE_CALC_ID, arrayOf(id.toString())) > 0).also { Log.v(this.javaClass.name,"<<<=== delete - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getToDate(key:Int,startDate:LocalDateTime,cutOffDate: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.d(this.javaClass.name,"<<<=== getToDate - Start")
            val db = dbConnect.readableDatabase
            val startDateStr = DateUtils.localDateTimeToString(startDate)
            val endDateStr = DateUtils.localDateTimeToString(cutOffDate)
            val selectionArgs = arrayOf(startDateStr, endDateStr)
            //selectionArgs.forEach (System.out::println)
            val cursor = db.query(
                CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
                COLUMNS_CALC,
                " ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ? ",
                arrayOf(key.toString()),
                null,
                null,
                null
            )
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor) {
                while (moveToNext()) {
                    val record = CreditCardBoughtMap().mapping(this)
                    if ( record.boughtDate == cutOffDate || record.boughtDate == startDate || record.boughtDate.isBefore(cutOffDate) and record.boughtDate.isAfter(
                            startDate
                        )
                    ) {
                        items.add(record)
                    }
                }
            }

            return items.also { Log.d(this.javaClass.name,"<<<=== getToDate - End ${it.size}") }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingQuotes(key:Int,startDate: LocalDateTime,cutoffCurrent: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.d(this.javaClass.name,"<<<=== getPendingQuotes - Start")
        val db = dbConnect.readableDatabase
        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_CALC," ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH} > ? and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ? ",
            arrayOf(1.toString(),key.toString()),null,null,null)
        val items = mutableListOf<CreditCardBoughtDTO>()
        with(cursor){
            while(moveToNext()){
                val record = CreditCardBoughtMap().mapping(this)
                val difference = DateUtils.getMonths(record.boughtDate,cutoffCurrent)
                Log.d(this.javaClass.name," ${record.boughtDate} < $startDate -  $difference < ${record.month}")
                if(record.boughtDate.isBefore(startDate) &&
                    difference < record.month ) {
                    Log.d(this.javaClass.name,"Added")
                    items.add(record)
                }
            }
        }
        return items.also { Log.d(this.javaClass.name,"<<<=== getPendingQuotes - End ${it.size}") }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapital(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getCapital - Start")
            val list = getToDate(key,startDate, cutOff)
            val listRecurrent = getRecurrentBuys(key,cutOff)
            val capitalRecurrent = listRecurrent.filter{ it.month == 1}.map { it.valueItem }.reduceOrNull{ val1,val2 -> val1.add(val2)}?:BigDecimal.ZERO
            val capitalRecurrentQuotes = listRecurrent.filter{it.month > 1}.map{ it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)}.reduceOrNull { val1, val2 -> val1.add(val2)}?:BigDecimal.ZERO
            val capital = list.filter{it.month == 1}.map{it.valueItem}.reduceOrNull{val1,val2->val1.plus(val2)}?:BigDecimal.ZERO
            val capitalQuotes = list.filter{it.month > 1}.map{it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)}.reduceOrNull{ val1, val2->val1.plus(val2)}?:BigDecimal.ZERO
            return Optional.ofNullable(capital.add(capitalQuotes).add(capitalRecurrent).add(capitalRecurrentQuotes)).also { Log.v(this.javaClass.name,"<<<=== getCapital - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTax(codCreditCard:Long,cutOff:LocalDateTime, kind:TaxEnum):Optional<Double>{
        Log.d(this.javaClass.name,"<<<=== getTax - Start ${cutOff.month} ${cutOff.month.value} ${cutOff.year}")
            val taxSvc = TaxImpl(dbConnect)
            val tax = taxSvc.get(codCreditCard,cutOff.month.value, cutOff.year,kind)
            if (tax.isPresent) {
                return Optional.ofNullable(tax.get().value).also { Log.d(this.javaClass.name,"<<<=== getTax - End ${it.get()}") }
            }
            return Optional.empty<Double>().also { Log.d(this.javaClass.name,"<<<=== getTax - End Not found Tax") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCreditCardSetting(codeBoughtCreditCard: Int):Optional<CreditCardSettingDTO>{
        val buyCCSDto = buyCCSettingSvc.get(codeBoughtCreditCard)
        if(buyCCSDto.isPresent){
            return creditCardSettingSvc.get(buyCCSDto.get().codeCreditCardSetting)
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInterestValue(creditCardBoughtDTO: CreditCardBoughtDTO,taxDTO: Optional<Double>,taxCashAdv:Optional<Double>):Double{
        val settingDto = getCreditCardSetting(creditCardBoughtDTO.id)
        if(settingDto.isPresent){
            return settingDto.get().value.toDouble()
        }
        var interest = creditCardBoughtDTO.interest
        when(creditCardBoughtDTO.kind){
            TaxEnum.CREDIT_CARD.ordinal.toShort() ->{
                interest = taxDTO.orElse(0.0)
            }
            TaxEnum.CASH_ADVANCE.ordinal.toShort()->{
                interest = taxCashAdv.orElse(0.0)
            }
        }
        return interest
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterest(codCreditCard: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== getInterest - Start")
            val listRecurrent = getRecurrentBuys(codCreditCard,cutOff)
        Log.v(this.javaClass.name,"List Recurrent: $listRecurrent")
            val interestRecurrent = listRecurrent.stream().filter{ it.month > 1}.map {
                val interest = it.interest / 100
                Log.d(this.javaClass.name," InterestRec:: ${it.valueItem} X ${interest}  = ${it.valueItem.multiply(interest.toBigDecimal())}")
                it.valueItem.multiply(interest.toBigDecimal())
            }
                .peek{Log.v(this.javaClass.name," InterestRec:: $it")}
                .reduce{ accumulator, interest -> accumulator.add(interest)}
        Log.v(this.javaClass.name,"InterestRecurrent:: $interestRecurrent")
            val tax = getTax(codCreditCard.toLong(),cutOff,TaxEnum.CREDIT_CARD)

            val taxCashAdv = getTax(codCreditCard.toLong(),cutOff,TaxEnum.CASH_ADVANCE)
            val list = getToDate(codCreditCard,startDate,cutOff)
            var value = list.stream().filter{ it.month > 1}
                                 .map{
                                     val interest = getInterestValue(it,tax,taxCashAdv)
                                     Log.v(this.javaClass.name,"${it.valueItem} X ${interest}%")
                                     it.valueItem.multiply(interest.toBigDecimal().divide(BigDecimal(100),8,RoundingMode.CEILING))
                                 }
                                 .reduce{ accumulator  ,interest -> accumulator.add(interest)}
        Log.v(this.javaClass.name," Interest Calc:: Interest: $value Recurrent: $interestRecurrent")
            value = Optional.ofNullable(value.orElse(BigDecimal(0)).add(interestRecurrent.orElse(BigDecimal.ZERO)))
        return value.also { Log.d(this.javaClass.name,"<<<=== FINISH getInterest $value ") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalPendingQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getCapitalPendingQuotes - Start")
        val list = getPendingQuotes(key,startDate,cutOff)
        val value = list.stream().map {
            val months = DateUtils.getMonths(it.boughtDate,cutOff)
            val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
            val bought = quote.multiply(months.toBigDecimal())
            val capital = it.valueItem.minus(bought)
            if(capital > BigDecimal(0)){
                Log.v(this.javaClass.name," Calculate Quite: ${it.valueItem}/${it.month} = $quote")
                 quote
            }else {
                BigDecimal(0)
            }
        }.reduce{val1,val2->val1.plus(val2)}
        return value.also { Log.v(this.javaClass.name,"<<<=== getCapitalPendingQuotes - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestPendingQuotes(codCreditCard: Int,startDate: LocalDateTime, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== getInterestPendingQuotes - Start")
            val tax = getTax(codCreditCard.toLong(),cutOff,TaxEnum.CREDIT_CARD)
            val taxCashAdv = getTax(codCreditCard.toLong(),cutOff,TaxEnum.CASH_ADVANCE)
        val list = getPendingQuotes(codCreditCard,startDate,cutOff)
        val value = list.stream().map {
            val months = DateUtils.getMonths(it.boughtDate,cutOff)
            val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
            val bought = quote.multiply(months.toBigDecimal())
            val capital = it.valueItem.minus(bought)
            Log.v(this.javaClass.name,"Month $months; Quote $quote; Bought $bought; Capital $capital; Value ${it.valueItem}; Date ${it.boughtDate}")
            if(capital > BigDecimal(0)){
                val interest = getInterestValue(it, tax,taxCashAdv)
                Log.v(this.javaClass.name,"$capital X ${interest}%")
                capital.multiply(interest.div(100).toBigDecimal())
            }else{
                BigDecimal(0)
            }
        }.reduce{val1,val2->val1.plus(val2)}
        return value.also { Log.d(this.javaClass.name,"<<<=== getInterestPendingQuotes - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBought(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<Long> {
        Log.d(this.javaClass.name,"<<<=== getBought - Start")
            val list = getToDate(key, startDate,cutOff)
            val listRecurrent = getRecurrentBuys(key,cutOff)
            val countRecurrent = listRecurrent.count { it.month == 1}
            val value = list.count { it.month <= 1 }
            return Optional.ofNullable((value + countRecurrent).toLong()).also { Log.d(this.javaClass.name,"<<<=== getBought - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBoughtPendingQuotes(key: Int, startDate: LocalDateTime,cutOff: LocalDateTime): Optional<Long> {
        Log.d(this.javaClass.name,"<<<=== getBoughtPendingQuotes - Start")
            val list = getPendingQuotes(key, startDate,cutOff)
            val value = list.stream().filter {
                val months = DateUtils.getMonths(it.boughtDate,cutOff)
                months <= it.month
            }.count()
            return Optional.ofNullable(value).also{Log.d(this.javaClass.name,"<<<=== getBoughtPendingQuotes - End $it")}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBoughtQuotes(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<Long> {
        Log.d(this.javaClass.name,"<<<=== getBoughtQuotes - Start")
            val list = getToDate(key,startDate,cutOff)
            val listRecurrent = getRecurrentBuys(key,cutOff)
            val countRecurrent = listRecurrent.count{ it.month > 1}
            val value =  list.count{ it.month > 1 }
            return Optional.ofNullable((value+countRecurrent).toLong()).also { Log.d(this.javaClass.name,"<<<=== getBoughtQuotes - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPay(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== getPendingToPay - Start")
            val list = getToDate(key,startDate,cutOff)
            val value = list.stream().map {
                val months = DateUtils.getMonths(it.boughtDate,cutOff)
                val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
                val bought = quote.multiply(months.toBigDecimal())
                val capital = it.valueItem.minus(bought)
                if(capital > BigDecimal(0) && it.month > 1){
                    capital
                }else{
                    BigDecimal(0)
                }
            }.reduce{val1,val2->val1.plus(val2)}
            return value.also { Log.d(this.javaClass.name,"<<<=== getPendingToPay - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPayQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== getPendingToPayQuotes - Start")
            val list = getPendingQuotes(key,startDate,cutOff)
            val value = list.stream().map {
                val months = DateUtils.getMonths(it.boughtDate,cutOff)
                val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
                val bought = quote.multiply(months.toBigDecimal())
                Log.d(this.javaClass.name,"${it.valueItem} - $bought")
                val capital = it.valueItem.minus(bought)
                if(capital > BigDecimal(0)){
                    capital
                }else{
                    BigDecimal(0)
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
            " id = ?",
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
        Log.d(this.javaClass.name,"<<<=== getRecurrentBuys - Start")
            val db = dbConnect.readableDatabase
            val cutOffLastMonth = cutOff.minusMonths(1)
            val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_CALC," ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_RECURRENT} = ? and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ? ",
                arrayOf(1.toString(),key.toString()),null,null,null)
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor){
                while(moveToNext()){
                    val record = CreditCardBoughtMap().mapping(this)
                    Log.d(this.javaClass.name," ${record.boughtDate} < $cutOffLastMonth")
                    if(record.boughtDate.isBefore(cutOffLastMonth)) {
                        Log.d(this.javaClass.name,"Added")
                        record.boughtDate = LocalDateTime.of(cutOff.year,cutOff.month,record.boughtDate.dayOfMonth,record.boughtDate.hour,record.boughtDate.minute)
                        items.add(record)
                    }
                }
            }
            return items.also { Log.d(this.javaClass.name,"<<<=== getRecurrentBuys - End ${it.size}") }
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

}