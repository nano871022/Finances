package co.japl.finances.core.usercases.implement.recap

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.finances.core.R
import co.japl.finances.core.adapters.outbound.interfaces.IBuyCreditCardSettingPort
import co.japl.finances.core.adapters.outbound.interfaces.ICreditCardPort
import co.japl.finances.core.adapters.outbound.interfaces.ICreditCardSettingPort
import co.japl.finances.core.adapters.outbound.interfaces.IDifferInstallmentRecapPort
import co.japl.finances.core.adapters.outbound.interfaces.IQuoteCreditCardPort
import co.japl.finances.core.adapters.outbound.interfaces.ITagQuoteCreditCardPort
import co.japl.finances.core.adapters.outbound.interfaces.ITaxPort
import co.japl.finances.core.dto.CreditCardBoughtDTO
import co.japl.finances.core.dto.CreditCardDTO
import co.japl.finances.core.enums.KindOfTaxEnum
import co.japl.finances.core.enums.TaxEnum
import co.japl.finances.core.usercases.interfaces.recap.IQuoteCreditCard
import co.japl.finances.core.usercases.mapper.CreditCardMap
import co.japl.finances.core.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.ArrayList
import java.util.Optional
import javax.inject.Inject

class QuoteCreditCardImpl @Inject constructor(private val creditCardSvc:ICreditCardPort
, private val quoteCCImpl: IQuoteCreditCardPort
,private val differInstallmentSvc: IDifferInstallmentRecapPort
,private val taxSvc:ITaxPort
,private val buyCCSettingSvc: IBuyCreditCardSettingPort
,private val creditCardSettingSvc:ICreditCardSettingPort
,private val tagQuoteCreditSvc:ITagQuoteCreditCardPort
,private val context:Context): IQuoteCreditCard {
    override fun getTotalQuote(): BigDecimal {
        val creditCards = creditCardSvc.getAll().filter { it.status }
        var quote = BigDecimal.ZERO
        for ( creditCard in creditCards) {
            val creditCardPojo = CreditCardMap().mapper(creditCard)
            val endDate = DateUtils.cutOffLastMonth(creditCardPojo.cutoffDay.get())
            val startDate = DateUtils.startDateFromCutoff(creditCardPojo.cutoffDay.get(),endDate)
            val capital = getCapital(creditCard.id, startDate, endDate)?:BigDecimal.ZERO
            val capitalQuotes =
                getCapitalPendingQuotes(creditCard.id, startDate, endDate)?:BigDecimal.ZERO
            val interest = getInterest(creditCard.id, startDate, endDate)?:BigDecimal.ZERO
            val interestQuote =
                getInterestPendingQuotes(creditCard.id, startDate, endDate)?:BigDecimal.ZERO
            quote += capital + capitalQuotes+ interest + interestQuote
        }
        return quote
    }

    override fun getCapital(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime): BigDecimal {
        Log.v(this.javaClass.name,"<<<=== getCapital - Start")
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())
        val list = quoteCCImpl.getToDate(key,startDate, cutOff)
        val capitalRecurrent = quoteCCImpl.getRecurrentBuys(key,cutOff).map { it.valueItem / it.month.toBigDecimal() }.reduceOrNull{ val1,val2 -> val1.add(val2)}?:BigDecimal.ZERO
        val capital = list.filter{it.month == 1}.map{it.valueItem}.reduceOrNull{val1,val2->val1.plus(val2)}?:BigDecimal.ZERO
        val capitalQuotes = list.filter{it.month > 1}.map{
            differQuotes.firstOrNull { differ->differ.cdBoughtCreditCard.toInt() == it.id }?.let {
                return (it.pendingValuePayable / it.newInstallment).toBigDecimal()
            }?:(it.valueItem / it.month.toBigDecimal())
        }.reduceOrNull{ val1, val2->val1.plus(val2)}?:BigDecimal.ZERO
        return (capital.add(capitalQuotes).add(capitalRecurrent)).also { Log.v(this.javaClass.name,"<<<=== getCapital - End $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalPendingQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime): BigDecimal {
        Log.v(this.javaClass.name,"<<<=== STARTING::getCapitalPendingQuotes ")
        val differQuotes = differInstallmentSvc.get(cutOff.toLocalDate())

        val list = quoteCCImpl.getPendingQuotes(key,startDate,cutOff).toMutableList()
        quoteCCImpl.getRecurrentPendingQuotes(key, cutOff)?.let{list.addAll(it)}
        val value = list.stream().map {
            val differ = differQuotes.firstOrNull{differ->differ.cdBoughtCreditCard.toInt() == it.id}
            val months:Long = differ?.let{
                DateUtils.getMonths(LocalDateTime.of(it.create, LocalTime.MAX),cutOff)
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
        return value.also { Log.v(this.javaClass.name,"<<<=== ENDING::getCapitalPendingQuotes $it") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterest(codCreditCard: Int, startDate:LocalDateTime,cutOff: LocalDateTime): BigDecimal {
        Log.d(this.javaClass.name,"<<<=== START: getInterest ")
        val tax = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CREDIT_CARD)
        val taxCashAdv = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CASH_ADVANCE)
        val taxWalletBuy = getTax(codCreditCard.toLong(),cutOff, TaxEnum.WALLET_BUY)

        val creditCard = creditCardSvc.get(codCreditCard) ?: return BigDecimal.ZERO

        val firstQuote = if(creditCard.interest1Quote) 1 else 0

        val listRecurrent = quoteCCImpl.getRecurrentBuys(codCreditCard,cutOff).toMutableList()
        listRecurrent.forEach{
            it.boughtDate = it.boughtDate.withYear(cutOff.year).withMonth(cutOff.monthValue)
        }
        val interestRecurrent = listRecurrent.stream().filter{ it.month > firstQuote}
            .map {calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}
            .reduce{ accumulator, interest -> accumulator.add(interest)}
        val list = quoteCCImpl.getToDate(codCreditCard,startDate,cutOff)

        val value = list.stream().filter{ it.month > firstQuote}
            .map{calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}
            .reduce{ accumulator  ,interest -> accumulator.add(interest)}
        return value.orElse(BigDecimal(0)).add(interestRecurrent.orElse(BigDecimal.ZERO))
            .also { Log.d(this.javaClass.name,"<<<=== FINISH: getInterest Interest: $value Recurrent: $interestRecurrent  Response. $it ") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestPendingQuotes(codCreditCard: Int,startDate: LocalDateTime, cutOff: LocalDateTime): BigDecimal? {
        Log.d(this.javaClass.name,"<<<=== STARTING::getInterestPendingQuotes Cod Credit Card: $codCreditCard Start: $startDate CutOff: $cutOff")
        val creditCard = creditCardSvc.get(codCreditCard)
        val tax = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CREDIT_CARD)
        val taxCashAdv = getTax(codCreditCard.toLong(),cutOff, TaxEnum.CASH_ADVANCE)
        val taxWalletBuy = getTax(codCreditCard.toLong(),cutOff, TaxEnum.WALLET_BUY)

        val list = quoteCCImpl.getPendingQuotes(codCreditCard,startDate,cutOff)
        val listRecurrent = quoteCCImpl.getRecurrentPendingQuotes(codCreditCard,cutOff)
        val value = list.stream().map {calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}.reduce{val1,val2->val1 + val2}.orElse(BigDecimal.ZERO)
        val valueRecurrent = listRecurrent.stream().map { calculateInterest(creditCard,it,tax,taxCashAdv,taxWalletBuy,cutOff)}.reduce{val1,val2->val1 + val2}.orElse(BigDecimal.ZERO)
        return (value + valueRecurrent).also { Log.d(this.javaClass.name,"<<<=== ENDING::getInterestPendingQuotes  $it") }
    }

    private fun getTax(codCreditCard:Long,cutOff:LocalDateTime, kind: TaxEnum):Pair<Double,String>?{
        Log.d(this.javaClass.name,"<<<=== START: getTax ${cutOff.month} ${cutOff.month.value} ${cutOff.year}")
        var tax = taxSvc.get(codCreditCard.toInt(),cutOff.month.value, cutOff.year,kind)
        if (tax != null) {
            return (Pair(tax.value,tax.kindOfTax?: KindOfTaxEnum.MONTHLY_EFFECTIVE.getName())).also { Log.d(this.javaClass.name,"<<<=== FINISH: getTax $it") }
        }
        tax = taxSvc.get(codCreditCard.toInt(), LocalDate.now().month.value, LocalDate.now().year,kind)
        if (tax != null) {
            return (Pair(tax.value,tax.kindOfTax?: KindOfTaxEnum.MONTHLY_EFFECTIVE.getName())).also { Log.d(this.javaClass.name,"<<<=== FINISH: getTax $it") }
        }
        return (null).also { Log.d(this.javaClass.name,"<<<=== FINISH: getTax - End Not found Tax") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateInterest(creditCard:CreditCardDTO?,dto:CreditCardBoughtDTO,tax: Pair<Double, String>?,taxCashAdv: Pair<Double, String>?,taxWalletBuy: Pair<Double, String>?,cutOff: LocalDateTime):BigDecimal{
        Log.d(javaClass.name,"=== calculateInterest: ${dto.nameItem} ${dto.boughtDate}")
        val differQuote = differInstallmentSvc.get(dto.id)
        val defaultTax = Pair(dto.interest,dto.kindOfTax)
        val month:Long = differQuote?.let{
            DateUtils.getMonths(LocalDateTime.of(it.create,LocalTime.MAX),cutOff)
        }?: DateUtils.getMonths(dto.boughtDate,cutOff)

        val interest = KindOfTaxImpl.getNM(tax?.first ?: defaultTax.first,KindOfTaxEnum.valueOf(tax?.second?:defaultTax.second)?:KindOfTaxEnum.MONTHLY_EFFECTIVE)
        val interestCashAdv = KindOfTaxImpl.getNM(taxCashAdv?.first ?:defaultTax.first,KindOfTaxEnum.valueOf(taxCashAdv?.second?:defaultTax.second)?:KindOfTaxEnum.MONTHLY_EFFECTIVE)
        val interestWalletBuy = KindOfTaxImpl.getNM(taxWalletBuy?.first ?:defaultTax.first,KindOfTaxEnum.valueOf(taxCashAdv?.second?:defaultTax.second)?:KindOfTaxEnum.MONTHLY_EFFECTIVE)
        var setting = false
        buyCCSettingSvc.getAll().forEach { Log.d(javaClass.name,"=== $it") }
        buyCCSettingSvc.get(dto.id)?.let {
            Log.d(javaClass.name,"=== Get Buy Setting: $it")
            creditCardSettingSvc.get(it.codeCreditCardSetting)?.let{
                Log.d(javaClass.name,"=== Get CC Setting: $it")
                setting = true
            }
        }

        return if(setting){
            (dto.valueItem / dto.month.toBigDecimal()) * dto.interest.toBigDecimal().also { Log.d(javaClass.name," calculateInterest: setting ${dto.valueItem} Interest: $interestWalletBuy Reponse: $it") }
        }else if(dto.month == 1 && creditCard?.interest1Quote == true && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD && differQuote == null){
            BigDecimal.ZERO.also { Log.d(javaClass.name," calculateInterest: 1 quote 0") }
        }else if(dto.month > 1 && month == 0L && creditCard?.interest1NotQuote == true && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD && differQuote == null){
            BigDecimal.ZERO.also { Log.d(javaClass.name," calculateInterest: 1 not quote") }
        }else if(dto.month > 1 && month == 1L && creditCard?.interest1NotQuote == true && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD){
            (dto.valueItem.multiply(interest.toBigDecimal()) + ((dto.valueItem - ((dto.valueItem/dto.month.toBigDecimal()) * month.toBigDecimal())) * interest.toBigDecimal())).also { Log.d(javaClass.name," calculateInterest: 1L ${dto.valueItem} Interest: $interest Response: $it") }
        }else if(dto.month > 1 && month > 1 && TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CREDIT_CARD || differQuote != null){
            val capital:Double = differQuote?.let{
                it.pendingValuePayable / it.newInstallment
            }?: (dto.valueItem.toDouble() / dto.month)
            val paid = capital * month
            val  lack:Double = differQuote?.let{
                it.pendingValuePayable - paid
            } ?:(dto.valueItem.toDouble() - paid)
            (lack* interest).toBigDecimal().also { Log.d(javaClass.name," calculateInterest: month > 1 ${dto.valueItem} Month: ${dto.month} Diff: $month Capital: $capital Paid: $paid lack: $lack Interest: $interest Response: $it") }
        }else if(TaxEnum.findByOrdinal(dto.kind) == TaxEnum.CASH_ADVANCE){
            dto.valueItem.multiply(interestCashAdv.toBigDecimal()).also { Log.d(javaClass.name," calculateInterest: cash ${dto.valueItem} Interest: $interestCashAdv Response: $it") }
        }else if(TaxEnum.findByOrdinal(dto.kind) == TaxEnum.WALLET_BUY){
            dto.valueItem.multiply(interestWalletBuy.toBigDecimal()).also { Log.d(javaClass.name," calculateInterest: wallet ${dto.valueItem} Interest: $interestWalletBuy Reponse: $it") }
        }else{
            dto.valueItem.multiply(interest.toBigDecimal()).also { Log.d(javaClass.name," calculateInterest: else $dto Interest: $interest Response: $it") }
        }
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
        val startDate = DateUtils.startDateFromCutoff(creditCard?.cutOffDay!!,cutOff)
        val list = quoteCCImpl.getToDate(codCreditCard,startDate,cutOff)
        val listRecurrent = quoteCCImpl.getRecurrentBuys(codCreditCard,cutOff)
        listRecurrent.forEach { it.boughtDate = it.boughtDate.withYear(cutOff.year).withMonth(cutOff.monthValue) }
        val listRecurrentPending = quoteCCImpl.getRecurrentPendingQuotes(codCreditCard,cutOff)
        val pending = quoteCCImpl.getPendingQuotes(codCreditCard,startDate,cutOff)
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
    private fun getQuoteValue(creditCard: CreditCardDTO?, bought:CreditCardBoughtDTO, tax: Pair<Double, String>?, taxCashAdv: Pair<Double, String>?, taxWalletBuy: Pair<Double, String>?, cutOff:LocalDateTime):BigDecimal{
        val interest = calculateInterest(creditCard,bought,tax,taxCashAdv,taxWalletBuy,cutOff)
        val capital = bought.valueItem.toDouble() / bought.month.toDouble()
        return (capital + (interest.toDouble())).toBigDecimal()
    }

}