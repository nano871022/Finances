package co.japl.android.myapplication.bussiness.impl

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.contentValuesOf
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.*
import co.japl.android.myapplication.bussiness.interfaces.ITagQuoteCreditCardSvc
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CreditCardBoughtMap
import co.japl.android.myapplication.bussiness.mapping.CreditCardMap
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.DifferInstallmentImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GracePeriodImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.TagQuoteCreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.PeriodsMap
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import com.google.android.gms.common.util.DataUtils
import com.google.gson.Gson
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class SaveCreditCardBoughtImpl @Inject constructor(val context:Context, override var dbConnect: SQLiteOpenHelper) :IQuoteCreditCardSvc{
    private val kindOfTaxSvc = KindOfTaxImpl()
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
    private val differInstallmentSvc = DifferInstallmentImpl(dbConnect)
    private val tagQuoteCreditSvc:ITagQuoteCreditCardSvc = TagQuoteCreditCardImpl(dbConnect)
    private val FORMAT_DATE_BOUGHT_WHERE = "substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},7,4)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},4,2)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE},1,2)"
    private val FORMAT_DATE_END_WHERE = "substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE},7,4)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE},4,2)||'-'||substr(${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE},1,2)"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardBoughtDTO): Long {
        Log.v(this.javaClass.name,"<<<=== save - Start")
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
                )!!).also { Log.v(this.javaClass.name, "<<<=== END:Save  $it") }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPeriods(creditCardId:Int):List<PeriodDTO>{
        Log.i(this.javaClass.name,"<<<=== START Get Periods")
        val db = dbConnect.readableDatabase
        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_PERIOD
            ,"${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ?",
            arrayOf(creditCardId.toString()),null,null,null)
        val creditCardDto = creditCardSvc.get(creditCardId)
        val items = mutableListOf<PeriodDTO>()
        with(cursor){
            while(moveToNext()) {
                val map = PeriodsMap(taxSvc,buyCCSettingSvc,creditCardSettingSvc,creditCardSvc as CreditCardImpl).maping(this,creditCardDto.get().cutOffDay.toInt())
                if(map.isPresent) {
                    Log.d(
                        this.javaClass.name,
                        "ID $creditCardId Capital: ${map.get().capital} Interest: ${map.get().interest} Total: ${map.get().total} CutOff: ${map.get().periodEnd}"
                    )
                    items.add(map.get())
                }
            }
        }
            val responseList = items.groupBy { it.periodStart }.map { (startPeriod,items) -> PeriodDTO(creditCardId,startPeriod,
                items[0].periodEnd,items.sumOf{it.interest},items.sumOf{it.capital},items.sumOf{it.total})}
                .sortedByDescending { it.periodStart }

            responseList.forEach { map ->
                    val capital = getCapital(creditCardId,map.periodStart,map.periodEnd)
                    val interest = getInterest(creditCardId,map.periodStart,map.periodEnd)
                    map.capital = capital.orElse(BigDecimal.ZERO)
                    map.interest = interest.orElse(BigDecimal.ZERO)
                    map.total = map.capital.add(map.interest)
                val interestPending =
                    getInterestPendingQuotes(creditCardId, map.periodStart, map.periodEnd)
                val capitalPending =
                    getCapitalPendingQuotes(creditCardId, map.periodStart, map.periodEnd)
                map.interest = map.interest.add(interestPending.orElse(BigDecimal.ZERO))
                map.capital = map.capital.add(capitalPending.orElse(BigDecimal.ZERO))
                    map.total = map.capital.add(map.interest)
            }

        return responseList.also { Log.i(this.javaClass.name,"<<<=== FINISH Get Periods Count ${it.size}") }
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
                    AND ($FORMAT_DATE_BOUGHT_WHERE between ? and ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE} between ? and ?)
                    AND ($FORMAT_DATE_END_WHERE > ? OR ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_END_DATE} > ?)
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
                    ?.takeIf {getValidMonths(it,cutoffCurrent,startDate)}
                    ?.let{items.add(it)}
            }
        }
        return items.also { Log.d(this.javaClass.name,"<<<=== FINISH::getPendingQuotes Size: ${it.size}") }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapital(key: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getCapital - Start")
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())
            val list = getToDate(key,startDate, cutOff)
            val capitalRecurrent = getRecurrentBuys(key,cutOff).map { it.valueItem / it.month.toBigDecimal() }.reduceOrNull{ val1,val2 -> val1.add(val2)}?:BigDecimal.ZERO
            val capital = list.filter{it.month == 1}.map{it.valueItem}.reduceOrNull{val1,val2->val1.plus(val2)}?:BigDecimal.ZERO
            val capitalQuotes = list.filter{it.month > 1}.map{
                differQuotes.firstOrNull { differ->differ.cdBoughtCreditCard.toInt() == it.id }?.let {
                    return Optional.of((it.pendingValuePayable / it.newInstallment).toBigDecimal())
                }?:(it.valueItem / it.month.toBigDecimal())
            }.reduceOrNull{ val1, val2->val1.plus(val2)}?:BigDecimal.ZERO
            return Optional.ofNullable(capital.add(capitalQuotes).add(capitalRecurrent)).also { Log.v(this.javaClass.name,"<<<=== getCapital - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTax(codCreditCard:Long,cutOff:LocalDateTime, kind: TaxEnum):Optional<Pair<Double,String>>{
        Log.d(this.javaClass.name,"<<<=== START: getTax ${cutOff.month} ${cutOff.month.value} ${cutOff.year}")
            val taxSvc = TaxImpl(dbConnect)
            var tax = taxSvc.get(codCreditCard,cutOff.month.value, cutOff.year,kind)
            if (tax.isPresent) {
                return Optional.ofNullable(Pair(tax.get().value,tax.get().kindOfTax?: KindOfTaxEnum.EM.name)).also { Log.d(this.javaClass.name,"<<<=== FINISH: getTax ${it.get()}") }
            }
            tax = taxSvc.get(codCreditCard,LocalDate.now().month.value, LocalDate.now().year,kind)
            if (tax.isPresent) {
                return Optional.ofNullable(Pair(tax.get().value,tax.get().kindOfTax?: KindOfTaxEnum.EM.name)).also { Log.d(this.javaClass.name,"<<<=== FINISH: getTax ${it.get()}") }
            }
        return Optional.empty<Pair<Double,String>>().also { Log.d(this.javaClass.name,"<<<=== FINISH: getTax - End Not found Tax") }
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
    private fun getInterestValue(creditCardBoughtDTO: CreditCardBoughtDTO,taxDTO: Optional<Pair<Double,String>>,taxCashAdv:Optional<Pair<Double,String>>):Double{
        val settingDto = getCreditCardSetting(creditCardBoughtDTO.id)
        if(settingDto.isPresent){
            if(settingDto.get().value != "0") {
                return kindOfTaxSvc.getNM(settingDto.get().value.toDouble(), KindOfTaxEnum.EM).also { Log.d(javaClass.name,"<<<=== ENDING::getInterestValue Response: $it") }
            }else{
                return 0.0.also { Log.d(javaClass.name,"<<<=== ENDING::getInterestValue Response: $it") }
            }
        }
        var interest = kindOfTaxSvc.getNM(creditCardBoughtDTO.interest, KindOfTaxEnum.valueOf(creditCardBoughtDTO.kindOfTax))
        when(creditCardBoughtDTO.kind){
            TaxEnum.CREDIT_CARD.ordinal.toShort() ->{
                interest = kindOfTaxSvc.getNM(taxDTO.get().first, KindOfTaxEnum.valueOf(taxDTO.get().second))
            }
            TaxEnum.CASH_ADVANCE.ordinal.toShort()->{
                interest = kindOfTaxSvc.getNM(taxCashAdv.get().first, KindOfTaxEnum.valueOf(taxDTO.get().second))
            }
        }
        return interest.also { Log.d(javaClass.name,"<<<=== ENDING::getInterestValue Response: $it") }
    }

    /**
     * FIRST QUOTE TO BOUGHT TO CREDIT
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterest(codCreditCard: Int, startDate:LocalDateTime,cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== START: getInterest ")
        val tax = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CREDIT_CARD)
        val taxCashAdv = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CASH_ADVANCE)
        val taxWalletBuy = getTax(codCreditCard.toLong(),cutOff, TaxEnum.WALLET_BUY)
        val creditCard = creditCardSvc.get(codCreditCard)
        val firstQuote = if(creditCard.get().interest1Quote) 1 else 0

        val listRecurrent = getRecurrentBuys(codCreditCard,cutOff).toMutableList()
        listRecurrent.forEach{
            it.boughtDate = it.boughtDate.withYear(cutOff.year).withMonth(cutOff.monthValue)
        }
        val interestRecurrent = listRecurrent.stream().filter{ it.month > firstQuote}
                .map {calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}
                .reduce{ accumulator, interest -> accumulator.add(interest)}
            val list = getToDate(codCreditCard,startDate,cutOff)

            val value = list.stream().filter{ it.month > firstQuote}
                            .map{calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}
                            .reduce{ accumulator  ,interest -> accumulator.add(interest)}
        return Optional.ofNullable(value.orElse(BigDecimal(0)).add(interestRecurrent.orElse(BigDecimal.ZERO)))
            .also { Log.d(this.javaClass.name,"<<<=== FINISH: getInterest Interest: $value Recurrent: $interestRecurrent  Response. $it ") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateInterest(creditCard:Optional<CreditCardDTO>,dto:CreditCardBoughtDTO,tax: Optional<Pair<Double, String>>,taxCashAdv: Optional<Pair<Double, String>>,taxWalletBuy: Optional<Pair<Double, String>>,cutOff: LocalDateTime):BigDecimal{
        Log.d(javaClass.name,"=== calculateInterest: ${dto.nameItem} ${dto.boughtDate}")
        val differQuote = differInstallmentSvc.get(dto.id)
        val defaultTax = Pair(dto.interest,dto.kindOfTax)
        val month:Long = differQuote?.takeIf { it.isPresent }?.let{
            DateUtils.getMonths(LocalDateTime.of(it.get().create,LocalTime.MAX),cutOff)
        }?: DateUtils.getMonths(dto.boughtDate,cutOff)

        val interest = kindOfTaxSvc.getNM(tax.orElse (defaultTax).first,KindOfTaxEnum.valueOf(tax.orElse(defaultTax).second)?:KindOfTaxEnum.EM)
        val interestCashAdv = kindOfTaxSvc.getNM(taxCashAdv.orElse(defaultTax).first,KindOfTaxEnum.valueOf(taxCashAdv.orElse(defaultTax).second)?:KindOfTaxEnum.EM)
        val interestWalletBuy = kindOfTaxSvc.getNM(defaultTax.first,KindOfTaxEnum.valueOf(defaultTax.second)?:KindOfTaxEnum.EM)
        Log.d(javaClass.name,"=== calculateInterest: Id: ${dto.id} Name: ${dto.nameItem} BoughtDate ${dto.boughtDate}  Month $month Tax: ${dto.interest} KindTax ${dto.kindOfTax}  Kind ${dto.kind} CC $interest CA $interestCashAdv WB $interestWalletBuy TCC $tax TCA $taxCashAdv TWB $taxWalletBuy DEF $defaultTax")
        var setting = false
        buyCCSettingSvc.getAll().forEach { Log.d(javaClass.name,"=== $it") }
        buyCCSettingSvc.get(dto.id).ifPresent {
            Log.d(javaClass.name,"=== calculateInterest id: ${dto.id} Get Buy Setting: $it")
            creditCardSettingSvc.get(it.codeCreditCardSetting).ifPresent{
                Log.d(javaClass.name,"=== calculateInterest id: ${dto.id}  Get CC Setting: $it")
                 setting = true
            }
        }

        return if(setting){
            (dto.valueItem / dto.month.toBigDecimal()) * dto.interest.toBigDecimal().also { Log.d(javaClass.name,"=== calculateInterest: id: ${dto.id} toPay: ${dto.valueItem} tax: $interestWalletBuy interest: $it SETTING") }
        }else if(dto.month == 1 && creditCard.get().interest1Quote && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD && !differQuote.isPresent){
            BigDecimal.ZERO.also { Log.d(javaClass.name,"=== calculateInterest: id: ${dto.id}  1 quote 0") }
        }else if(dto.month > 1 && month == 0L && creditCard.get().interest1NotQuote && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD && !differQuote.isPresent){
            BigDecimal.ZERO.also { Log.d(javaClass.name,"=== calculateInterest: id: ${dto.id}  1 not quote") }
        }else if(dto.month > 1 && month == 1L && creditCard.get().interest1NotQuote && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD){
            (dto.valueItem.multiply(interest.toBigDecimal()) + ((dto.valueItem - ((dto.valueItem/dto.month.toBigDecimal()) * month.toBigDecimal())) * interest.toBigDecimal())).also {
                Log.d(javaClass.name,"=== calculateInterest: id: ${dto.id}  toPay: ${dto.valueItem} Tax: $interest interest: $it 1L") }
        }else if(dto.month > 1 && month > 1 && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD || differQuote.isPresent){
            val capital:Double = differQuote.takeIf { it.isPresent }?.let{
                it.get().pendingValuePayable / it.get().newInstallment
            }?: (dto.valueItem.toDouble() / dto.month)
            val paid = capital * month
            val  lack:Double = differQuote.takeIf { it.isPresent }?.let{
                it.get().pendingValuePayable - paid
            } ?:(dto.valueItem.toDouble() - paid)
            (lack* interest).toBigDecimal().also { Log.d(javaClass.name,"=== calculateInterest: id: ${dto.id}  toPay: $lack Tax: $interest Interest: $it MONTHS month > 1 ${dto.valueItem} Month: ${dto.month} Diff: $month Capital: $capital Paid: $paid ") }
        }else if(TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CASH_ADVANCE){
            val capital:Double = differQuote.takeIf { it.isPresent }?.let{
                it.get().pendingValuePayable / it.get().newInstallment
            }?: (dto.valueItem.toDouble() / dto.month)
            val paid = capital * month
            val  lack:Double = differQuote.takeIf { it.isPresent }?.let{
                it.get().pendingValuePayable - paid
            } ?:(dto.valueItem.toDouble() - paid)
            lack.toBigDecimal().multiply(interestCashAdv.toBigDecimal()).also { Log.d(javaClass.name,"=== calculateInterest:  id: ${dto.id} toPay $lack Tax: $interestCashAdv Interes: $it CASH") }
        }else if(TaxEnum.findByOrdinal(dto.kind) == TaxEnum.WALLET_BUY){
            val capital:Double = differQuote.takeIf { it.isPresent }?.let{
                it.get().pendingValuePayable / it.get().newInstallment
            }?: (dto.valueItem.toDouble() / dto.month)
            val paid = capital * month
            val  lack:Double = differQuote.takeIf { it.isPresent }?.let{
                it.get().pendingValuePayable - paid
            } ?:(dto.valueItem.toDouble() - paid)
            lack.toBigDecimal().multiply(interestWalletBuy.toBigDecimal()).also { Log.d(javaClass.name,"=== calculateInterest: id: ${dto.id} toPay: $lack Tax: $interestWalletBuy interes: $it WALLET") }
        }else{
            dto.valueItem.multiply(interest.toBigDecimal()).also { Log.d(javaClass.name,"=== calculateInterest:  id: ${dto.id} toPay: ${dto.valueItem} Tax: ${interest} interest: $it DEFAULT") }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalPendingQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== STARTING::getCapitalPendingQuotes ")
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())

        val list = getPendingQuotes(key,startDate,cutOff).toMutableList()
        getRecurrentPendingQuotes(key, cutOff)?.let{list.addAll(it)}
        val value = list.stream().map {
            val differ = differQuotes.firstOrNull{differ->differ.cdBoughtCreditCard.toInt() == it.id}
            val months:Long = differ?.let{
               DateUtils.getMonths(LocalDateTime.of(it.create,LocalTime.MAX),cutOff)
            }?:DateUtils.getMonths(it.boughtDate,cutOff)


            val quote:BigDecimal = differ?.let{
               (it.pendingValuePayable / it.newInstallment).toBigDecimal()
            }?:(it.valueItem.toDouble() / it.month.toDouble()).toBigDecimal()

            val bought = quote.multiply(months.toBigDecimal())
            val capital:BigDecimal = differ?.let{
                (it.pendingValuePayable.minus(bought.toLong())).toBigDecimal()
            }?:(it.valueItem.toDouble() - bought.toDouble()).toBigDecimal()

            if(capital > BigDecimal.ZERO){
                 quote
            }else {
                BigDecimal.ZERO
            }
        }.reduce{val1,val2->val1.plus(val2)}.orElse(BigDecimal.ZERO)
        return Optional.of(value).also { Log.v(this.javaClass.name,"<<<=== ENDING::getCapitalPendingQuotes $it") }
    }

    /**
     * QUOTES PENDING MORE THAN ONE QUOTE TO PAID
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestPendingQuotes(codCreditCard: Int,startDate: LocalDateTime, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.d(this.javaClass.name,"<<<=== STARTING::getInterestPendingQuotes Cod Credit Card: $codCreditCard Start: $startDate CutOff: $cutOff")
        val creditCard = creditCardSvc.get(codCreditCard)
        val tax = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CREDIT_CARD)
        val taxCashAdv = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CASH_ADVANCE)
        val taxWalletBuy = getTax(codCreditCard.toLong(),cutOff, TaxEnum.WALLET_BUY)

        val list = getPendingQuotes(codCreditCard,startDate,cutOff)
        val listRecurrent = getRecurrentPendingQuotes(codCreditCard,cutOff)
        val value = list.stream().map {calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}.reduce{val1,val2->val1 + val2}.orElse(BigDecimal.ZERO)
        val valueRecurrent = listRecurrent.stream().map { calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}.reduce{val1,val2->val1 + val2}.orElse(BigDecimal.ZERO)
        return Optional.of(value + valueRecurrent).also { Log.d(this.javaClass.name,"<<<=== ENDING::getInterestPendingQuotes  $it") }
    }

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
    override fun getTotalQuoteTC(): BigDecimal {
        val creditCards = creditCardSvc.getAll().filter { it.status }
        var quote = BigDecimal.ZERO
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
            quote += capital.orElse(BigDecimal.ZERO) + capitalQuotes.orElse(BigDecimal.ZERO) + interest.orElse(
                BigDecimal.ZERO) + interestQuote.orElse(BigDecimal.ZERO)
        }
        return quote
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getDataToGraphStats(
        codCreditCard: Int,
        cutOff: LocalDateTime
    ): List<Pair<String, Double>> {
        val tax = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CREDIT_CARD)
        val taxCashAdv = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CASH_ADVANCE)
        val taxWalletBuy = getTax(codCreditCard.toLong(),cutOff, TaxEnum.WALLET_BUY)
        val creditCard = creditCardSvc.get(codCreditCard)
        val startDate = DateUtils.startDateFromCutoff(creditCard.get().cutOffDay,cutOff)
        val list = getToDate(codCreditCard,startDate,cutOff)
        val listRecurrent = getRecurrentBuys(codCreditCard,cutOff)
        listRecurrent.forEach { it.boughtDate = it.boughtDate.withYear(cutOff.year).withMonth(cutOff.monthValue) }
        val listRecurrentPending = getRecurrentPendingQuotes(codCreditCard,cutOff)
        val pending = getPendingQuotes(codCreditCard,startDate,cutOff)
        val joinList = ArrayList<CreditCardBoughtDTO>().toMutableList()
        joinList.addAll(list)
        joinList.addAll(pending)
        joinList.addAll(listRecurrent)
        joinList.addAll(listRecurrentPending)
        val withTags = joinList.filter { tagQuoteCreditSvc.getTags(it.id).isNotEmpty() }
        val tags = withTags.map {
            val tagName = tagQuoteCreditSvc.getTags(it.id).first().name
            val value = getQuoteValue(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)
            Pair(tagName,value.toDouble())
        }
        val withoutTags = joinList.filter { dto-> !withTags.any { it.id == dto.id } }
        val tags2 = tags.groupBy{ it.first }.mapValues { it.value.sumOf { it.second } }.map {Pair(it.key,it.value)}
        val oneMonth = withoutTags.filter { it.month == 1 && it.recurrent.toInt() == 0}.sumOf {getQuoteValue(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}
        val someMonth = withoutTags.filter { it.month > 1 && it.recurrent.toInt() == 0}.sumOf {getQuoteValue(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}
        val oneMonthRecurrent = withoutTags.filter { it.month == 1 && it.recurrent.toInt() == 1 }.sumOf { getQuoteValue(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff) }
        val someMonthRecurrent = withoutTags.filter { it.month > 1 && it.recurrent.toInt() == 1 }.sumOf { getQuoteValue(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff) }
        val join = mutableListOf<Pair<String,Double>>()
        tags2.takeIf { it.isNotEmpty() }?.let{join.addAll(tags2)}
        oneMonth.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_one_month),it.toDouble()))}
        someMonth.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_some_month),it.toDouble()))}
        oneMonthRecurrent.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_one_month_recurrent),it.toDouble()))}
        someMonthRecurrent.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_some_month_recurrent),it.toDouble()))}
        return join
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuoteValue(creditCard:Optional<CreditCardDTO>, bought:CreditCardBoughtDTO, tax: Optional<Pair<Double, String>>, taxCashAdv: Optional<Pair<Double, String>>, taxWalletBuy: Optional<Pair<Double, String>>, cutOff:LocalDateTime):BigDecimal{
        val interest = calculateInterest(creditCard,bought,tax,taxCashAdv,taxWalletBuy,cutOff)
        val capital = bought.valueItem.toDouble() / bought.month.toDouble()
        return (capital + (interest.toDouble())).toBigDecimal()
    }
}