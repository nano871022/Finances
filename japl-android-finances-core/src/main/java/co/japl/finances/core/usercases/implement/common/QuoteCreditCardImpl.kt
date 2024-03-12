package co.japl.finances.core.usercases.implement.common

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.finances.core.R
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.model.Tax
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.ValuesCalculation
import co.japl.finances.core.usercases.interfaces.common.IQuoteCreditCard
import co.japl.finances.core.usercases.mapper.CreditCardMap
import co.japl.finances.core.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class QuoteCreditCardImpl @Inject constructor(private val creditCardSvc:ICreditCardPort
, private val quoteCCImpl: IQuoteCreditCardPort
,private val differInstallmentSvc: IDifferInstallmentRecapPort
,private val tagQuoteCreditSvc:ITagQuoteCreditCardPort
,private val context:Context
,private val valuesCalculation: ValuesCalculation
    ,private val interestCalculation: InterestCalculations
    , private val listBoughts: ListBoughts
): IQuoteCreditCard {

    override fun getTotalQuote(cache:Boolean): BigDecimal {
        return creditCardSvc.getAll().filter { it.status }.sumOf {creditCard->
            val creditCardPojo = CreditCardMap.mapper(creditCard)
            val endDate = DateUtils.cutOffLastMonth(creditCardPojo.cutoffDay)
            val startDate = DateUtils.startDateFromCutoff(creditCardPojo.cutoffDay,endDate)
            val capital = getCapital(creditCard.id, startDate, endDate,cache)?:BigDecimal.ZERO
            val capitalQuotes =
                getCapitalPendingQuotes(creditCard.id, startDate, endDate,cache)?:BigDecimal.ZERO
            val interest = getInterest(creditCard.id, startDate, endDate,cache)?:BigDecimal.ZERO
            val interestQuote =
                getInterestPendingQuotes(creditCard.id, startDate, endDate,cache)?:BigDecimal.ZERO
            capital + capitalQuotes+ interest + interestQuote.also{
                Log.d(javaClass.name,"<<<=== getTotalQuote - Capital $capital Capital Quotes $capitalQuotes Interest $interest Interest Quote $interestQuote Response $it")
            }
        }
    }


    private fun sumCapitalValue(it:CreditCardBoughtDTO,differQuotes:List<DifferInstallmentDTO>):Double  {
        return valuesCalculation.getCapital(it.id,it.valueItem.toDouble(),it.month.toShort(),differQuotes)
    }

    override fun getCapital(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime,cache:Boolean): BigDecimal {
        Log.v(this.javaClass.name,"<<<=== getCapital - Start")
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())

        val list = quoteCCImpl.getToDate(key,startDate, cutOff,cache)

        val capitalRecurrent = quoteCCImpl.getRecurrentBuys(key, cutOff).sumOf {sumCapitalValue(it,differQuotes)}
        val capital = list.filter{it.month == 1}.sumOf{sumCapitalValue(it,differQuotes)}
        val capitalQuotes = list.filter{it.month > 1}.sumOf { sumCapitalValue(it,differQuotes) }

        return (capital + capitalQuotes + capitalRecurrent).toBigDecimal().also { Log.v(this.javaClass.name,"<<<=== getCapital - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalPendingQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime,cache:Boolean): BigDecimal {
        Log.v(this.javaClass.name,"<<<=== STARTING::getCapitalPendingQuotes ")
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())

        val list = quoteCCImpl.getPendingQuotes(key,startDate,cutOff,cache).toMutableList()
        quoteCCImpl.getRecurrentPendingQuotes(key, cutOff,cache)?.let{list.addAll(it)}
        val value = list.sumOf {
             valuesCalculation.getCapital(it.id,it.valueItem.toDouble(),it.month.toShort(),differQuotes)
        }
        return value.toBigDecimal().also { Log.v(this.javaClass.name,"<<<=== ENDING::getCapitalPendingQuotes $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterest(codCreditCard: Int, startDate:LocalDateTime,cutOff: LocalDateTime,cache:Boolean): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START: getInterest ")
        val period = YearMonth.from(cutOff)
        val tax = interestCalculation.getTax(codCreditCard, KindInterestRateEnum.CREDIT_CARD,period)
        val taxCashAdv = interestCalculation.getTax(codCreditCard, KindInterestRateEnum.CASH_ADVANCE,period)

        val creditCard = creditCardSvc.get(codCreditCard) ?: return BigDecimal.ZERO

        val firstQuote = if(creditCard.interest1Quote) 1 else 0

        val listRecurrent = quoteCCImpl.getRecurrentBuys(codCreditCard,cutOff).toMutableList()
        listRecurrent.forEach{
            it.boughtDate = it.boughtDate.withYear(cutOff.year).withMonth(cutOff.monthValue)
        }
        val interestRecurrent = listRecurrent.filter{ it.month > firstQuote}
            .sumOf {calculateInterest(creditCard,it,tax,taxCashAdv,cutOff)}

        val list = quoteCCImpl.getToDate(codCreditCard,startDate,cutOff,cache)

        val interestSomeMonths = list.filter{ it.month > firstQuote}
            .sumOf{calculateInterest(creditCard,it,tax,taxCashAdv,cutOff)}
        return interestSomeMonths + interestRecurrent
            .also { Log.d(this.javaClass.name,"<<<=== FINISH: getInterest Interest: $interestSomeMonths Recurrent: $interestRecurrent  Response. $it ") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestPendingQuotes(codCreditCard: Int,startDate: LocalDateTime, cutOff: LocalDateTime,cache:Boolean): BigDecimal? {
        Log.d(this.javaClass.name,"<<<=== STARTING::getInterestPendingQuotes Cod Credit Card: $codCreditCard Start: $startDate CutOff: $cutOff")
        val creditCard = creditCardSvc.get(codCreditCard)
        val period = YearMonth.from(cutOff)
        val tax = interestCalculation.getTax(codCreditCard, KindInterestRateEnum.CREDIT_CARD,period)
        val taxCashAdv = interestCalculation.getTax(codCreditCard, KindInterestRateEnum.CASH_ADVANCE,period)

        val list = quoteCCImpl.getPendingQuotes(codCreditCard,startDate,cutOff,cache)
        val listRecurrent = quoteCCImpl.getRecurrentPendingQuotes(codCreditCard,cutOff,cache)
        val value = list.sumOf {calculateInterest(creditCard,it,tax,taxCashAdv,cutOff)}
        val valueRecurrent = listRecurrent.sumOf { calculateInterest(creditCard,it,tax,taxCashAdv,cutOff)}
        return (value + valueRecurrent).also { Log.d(this.javaClass.name,"<<<=== ENDING::getInterestPendingQuotes  $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateInterest(creditCard:CreditCardDTO?,dto:CreditCardBoughtDTO,tax: Tax?,taxCashAdv: Tax?,cutOff: LocalDateTime):BigDecimal{
        Log.d(javaClass.name,"=== calculateInterest: ${dto.nameItem} ${dto.boughtDate}")
        val differQuote = differInstallmentSvc.get(dto.id)
        val settings = interestCalculation.getSettings(dto.id,dto.interest)
        val defaultTax = Tax(dto.interest,dto.kindOfTax)
        val interest = interestCalculation.calculateInterestMonthlyEffective(dto.kind,defaultTax,tax,taxCashAdv,settings)
        val monthsPaid = valuesCalculation.getQuotesPaid(dto.id,dto.month.toShort(),dto.boughtDate,cutOff,listOf(differQuote))
        val capitalValue = valuesCalculation.getCapital(dto.id,dto.valueItem.toDouble(),dto.month.toShort(),listOf(differQuote))
        val pendingToPay = valuesCalculation.getPendingToPay(dto.id,dto.month.toShort(),monthsPaid,dto.valueItem.toDouble(),capitalValue, listOf(differQuote))
        return interestCalculation.getInterestValue(dto.month.toShort(),monthsPaid,dto.kind, pendingToPay,dto.valueItem.toDouble(),interest.value,interest.kind
            ,creditCard?.interest1Quote?:false,creditCard?.interest1NotQuote?:false).toBigDecimal()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getDataToGraphStats(
        codCreditCard: Int,
        cutOff: LocalDateTime
        ,cache:Boolean
    ): List<Pair<String, Double>> {
        val period = YearMonth.from(cutOff)
        val tax = interestCalculation.getTax(codCreditCard, KindInterestRateEnum.CREDIT_CARD,period)
        val taxCashAdv = interestCalculation.getTax(codCreditCard, KindInterestRateEnum.CASH_ADVANCE,period)
        val creditCardDto = creditCardSvc.get(codCreditCard)
        val creditCard = CreditCardMap.mapper(creditCardDto!!)
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())

        val joinList = listBoughts.getList(creditCard,cutOff,cache)

        val withTags = joinList.filter { tagQuoteCreditSvc.getTags(it.id).isNotEmpty() }
        val tags = withTags.map {
            val tagName = tagQuoteCreditSvc.getTags(it.id).first().name
            val value = getQuoteValue(creditCard,it,tax,taxCashAdv,cutOff,differQuotes)
            Pair(tagName,value.toDouble())
        }
        val withoutTags = joinList.filter { dto-> !withTags.any { it.id == dto.id } }
        val tags2 = tags.groupBy{ it.first }.mapValues { it.value.sumOf { it.second } }.map {Pair(it.key,it.value)}
        val oneMonth = withoutTags.filter { it.month == 1 && !it.recurrent}.sumOf {getQuoteValue(creditCard,it,tax,taxCashAdv,cutOff,differQuotes)}
        val someMonth = withoutTags.filter { it.month > 1 && !it.recurrent}.sumOf {getQuoteValue(creditCard,it,tax,taxCashAdv,cutOff,differQuotes)}
        val oneMonthRecurrent = withoutTags.filter { it.month == 1 && it.recurrent }.sumOf { getQuoteValue(creditCard,it,tax,taxCashAdv,cutOff,differQuotes) }
        val someMonthRecurrent = withoutTags.filter { it.month > 1 && it.recurrent }.sumOf { getQuoteValue(creditCard,it,tax,taxCashAdv,cutOff,differQuotes) }
        val join = mutableListOf<Pair<String,Double>>()
        tags2.takeIf { it.isNotEmpty() }?.let{join.addAll(tags2)}
        oneMonth.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_one_month),it.toDouble()))}
        someMonth.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_some_month),it.toDouble()))}
        oneMonthRecurrent.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_one_month_recurrent),it.toDouble()))}
        someMonthRecurrent.takeIf { it > BigDecimal.ZERO }?.let{join.add(Pair(context.resources.getString(R.string.str_graph_qcc_some_month_recurrent),it.toDouble()))}
        return join
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuoteValue(creditCard: CreditCard, bought: CreditCardBoughtItemDTO, tax: Tax?, taxCashAdv: Tax?, cutOff:LocalDateTime,differQuotes: List<DifferInstallmentDTO>):BigDecimal{
        listBoughts.calculateValues(bought,creditCard,tax,taxCashAdv,cutOff,differQuotes)
        return bought.quoteValue.toBigDecimal()

    }

}

