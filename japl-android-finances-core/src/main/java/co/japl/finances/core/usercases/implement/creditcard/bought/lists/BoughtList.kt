package co.japl.finances.core.usercases.implement.creditcard.bought.lists

import android.util.Log
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.TaxEnum
import co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.ICreditCardSettingPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.model.Tax
import co.japl.finances.core.usercases.implement.common.KindOfTaxImpl
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.mapper.CreditCardBoughtItemMapper
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class BoughtList @Inject constructor(
    private val quoteCCSvc:IQuoteCreditCardPort
    ,private val taxScv: ITaxPort
    ,private val buyCreditCardSettingSvc: IBuyCreditCardSettingPort
    ,private val creditCardSettingSvc: ICreditCardSettingPort
    ,private val differQuotesSvc:IDifferInstallmentRecapPort
    ,private val tagsSvc:ITagQuoteCreditCardPort
    , private val creditCardSvc:ICreditCardPort
): IBoughtList {

    override fun getBoughtList(creditCard:CreditCard,cutoff:LocalDateTime): CreditCardBoughtListDTO {
        val recap = RecapCreditCardBoughtListDTO()
        val period = YearMonth.of(cutoff.year,cutoff.month)
        val list = getList(creditCard)
        val taxQuote = getTax(creditCard.codeCreditCard,TaxEnum.CREDIT_CARD,period)
        val taxAdvance = getTax(creditCard.codeCreditCard,TaxEnum.CASH_ADVANCE,period)
        val taxWalletBuy = getTax(creditCard.codeCreditCard,TaxEnum.WALLET_BUY,period)
        val differQuotes = differQuotesSvc.get(cutoff.toLocalDate())

        list.forEach { dto->

            tag(dto.id)?.let { dto.tagName = it.name }

            getTaxToQuote(dto,taxQuote,taxAdvance,taxWalletBuy).let {
                dto.interest = it.value
                dto.kindOfTax = it.kind
            }

            getQuotesBoughtTotal(dto,cutoff)?.let { dto.monthPaid = it}

            lastInterestCalc(dto,creditCard)?.let {
                dto.interest = it.value
                dto.kindOfTax = it.kind
            }

            getCapital(dto,differQuotes).let { dto.capitalValue = it}

            getPendingToPay(dto,differQuotes).let { dto.pendingToPay = it}

            getInterest(dto,creditCard,differQuotes).let { dto.interestValue = it}

            dto.quoteValue = (dto.interestValue?:0.0) + (dto.capitalValue?:0.0)

            Log.w(javaClass.name,"=== getBoughtList id: ${dto.id} toPay: ${dto.pendingToPay} Tax: ${dto.interest} ${dto.kindOfTax} interest: ${dto.interestValue}")

            recap.numBought += 1
            recap.numRecurrentBought += if(dto.recurrent) 1 else 0
            recap.numQuoteBought += if(dto.recurrent.not() && dto.month > 1 && dto.monthPaid > 0) 1 else 0
            recap.num1QuoteBought += if(dto.recurrent.not() && dto.month == 1) 1 else 0

            recap.currentCapital += if(dto.month == 1)dto.capitalValue else 0.0
            recap.currentInterest += if(dto.month == 1)dto.interestValue else 0.0
            recap.quoteCapital += if(dto.month > 1 && dto.monthPaid > 0) dto.quoteValue else 0.0
            recap.quoteInterest += if(dto.month > 1 && dto.monthPaid > 0) dto.interestValue else 0.0
            recap.totalCapital += dto.capitalValue
            recap.totalInterest += dto.interestValue
            recap.quoteValue += dto.quoteValue
            recap.pendingToPay += dto.pendingToPay

            recap.numQuoteEnd += if(dto.month == (dto.monthPaid + 1).toInt()) 1 else 0
            recap.totalQuoteEnd += if(dto.month == (dto.monthPaid + 1).toInt()) dto.quoteValue else 0.0
            recap.numNextQuoteEnd += if(dto.month == (dto.monthPaid + 2).toInt()) 1 else 0
            recap.totalNextQuoteEnd += if(dto.month == (dto.monthPaid + 2).toInt()) dto.quoteValue else 0.0
        }
        return CreditCardBoughtListDTO(list,recap)
    }

    override fun delete(codeBought: Int): Boolean {
        return quoteCCSvc.delete(codeBought)
    }

    override fun endingRecurrentPayment(codeBought: Int, cutoff: LocalDateTime): Boolean {
        return quoteCCSvc.endingRecurrentPayment(codeBought,cutoff)
    }

    override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime): Boolean {
        quoteCCSvc.get(codeBought)?.let {
            val newRecurrent = it.copy(
                endDate = LocalDateTime.of(9999, 12, 31, 0, 0)
                , createDate = LocalDateTime.now()
                , id = 0
                , valueItem = value.toBigDecimal()
                )
                newRecurrent.boughtDate = it.boughtDate.withYear(newRecurrent.createDate.year).withMonth(newRecurrent.createDate.monthValue)
            val origin = it.copy(
                    endDate = cutOff.minusMonths(1).plusDays(1)
                    ,createDate = LocalDateTime.now())
            return quoteCCSvc.create(newRecurrent).takeIf { response->response > 0}?.let{id->
                if(quoteCCSvc.update(origin)){
                    return true
                }else{
                    quoteCCSvc.delete(id)
                    return false
                }
            }?:false
        }
        return false
    }

    override fun differntInstallment(codeBought: Int, value: Long,cutOff:LocalDateTime): Boolean {
        quoteCCSvc.get(codeBought)?.let {
            val creditCard = creditCardSvc.get(it.codeCreditCard)
            val dayOfMonth:Short = creditCard?.cutOffDay ?:it.cutOutDate.dayOfMonth.toShort()
            val months = DateUtils.getMonths(it.boughtDate,cutOff)

            val differBought =  it.copy(month = (months + value).toInt()
                , createDate = LocalDateTime.now()
                , endDate =  DateUtils.cutOffAddMonth(dayOfMonth, cutOff, value)
                , id = 0)
            val origin = it.copy(endDate = DateUtils.cutOffLastMonth(dayOfMonth,cutOff))
            return quoteCCSvc.create(differBought).takeIf { response->response > 0}?.let{id->
                    if(quoteCCSvc.update(origin)){
                        return true
                    }else{
                        quoteCCSvc.delete(id)
                        return false
                    }
            }?: false
        }
        return false
    }

    private fun tag(codeBought:Int):TagDTO?{
        return tagsSvc.getTags(codeBought).firstOrNull()
    }

    private fun lastInterestCalc(dto:CreditCardBoughtItemDTO,creditCard: CreditCard):Tax?{
       return creditCard?.takeIf { (
                             ( it.interest1Quote    && dto.monthPaid == 0L && dto.month == 1) ||
                             ( it.interest1NotQuote && dto.monthPaid == 0L && dto.month > 1)
                            )  && dto.kind == TaxEnum.CREDIT_CARD}?.let{
            Tax(0.0,KindOfTaxEnum.ANUAL_EFFECTIVE)
        }
    }

    private fun getList(creditCard: CreditCard): List<CreditCardBoughtItemDTO> {
        val startDate = DateUtils.startDateFromCutoff(creditCard.cutoffDay,creditCard.cutOff)
        val mainList = getMainList(creditCard,startDate)
        val recurrentList = getRecurrentList(creditCard)
        val recurrentPendingList = getRecurrentListPending(creditCard)
        val pendingList = getPendingList(creditCard,startDate)

        val list = ArrayList<CreditCardBoughtDTO>()
        list.addAll(mainList)
        list.addAll(recurrentList)
        list.addAll(recurrentPendingList)
        list.addAll(pendingList)

        return list.map (CreditCardBoughtItemMapper::mapper)
    }

    private fun getPendingList(creditCard:CreditCard,startDate: LocalDateTime): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getPendingQuotes(creditCard.codeCreditCard,startDate,creditCard.cutOff)
    }

    private fun getRecurrentListPending(creditCard: CreditCard): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getRecurrentPendingQuotes(creditCard.codeCreditCard,creditCard.cutOff).also{
            Log.d(javaClass.name,"=== PendingRecurrentList: ${it.size} $it")
        }
    }

    private fun getRecurrentList(creditCard:CreditCard): List<CreditCardBoughtDTO> {
        val list =  quoteCCSvc.getRecurrentBuys(creditCard.codeCreditCard,creditCard.cutOff)
        list.forEach {
            it.boughtDate = it.boughtDate
                .withYear(creditCard.cutOff.year)
                .withMonth(creditCard.cutOff.monthValue)
        }
        return list.also{
            Log.d(javaClass.name,"=== RecurrentList: ${it.size} $it")
        }
    }

    private fun getMainList(creditCard:CreditCard,startDate: LocalDateTime): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getToDate(creditCard.codeCreditCard,startDate,creditCard.cutOff).also {
            Log.d(javaClass.name,"=== MainList: ${it.size} $it")
        }
    }

    private fun getTax(codeCreditCard:Int,kindTax:TaxEnum,period:YearMonth):Tax?{
        return taxScv.get(codeCreditCard,period.monthValue,period.year,kindTax)?.let {
            Tax(it.value,KindOfTaxEnum.findByValue(it.kindOfTax?:KindOfTaxEnum.ANUAL_EFFECTIVE.getName()))
        }
    }


    private fun getCapital(dto:CreditCardBoughtItemDTO,differQuotes:List<DifferInstallmentDTO>): Double {
        return dto.month.takeIf { it > 1}?.let{
            differQuotes.firstOrNull { it.cdBoughtCreditCard.toInt() == dto.id }
                ?.let {
                    it.pendingValuePayable / it.newInstallment
                } ?: (dto.valueItem / dto.month)
        }?: dto.valueItem

    }


    private fun getTaxToQuote(creditCardBoughtItemDTO: CreditCardBoughtItemDTO, taxCCValue: Tax?, taxADVValue: Tax?, taxWBValue:Tax?):Tax{
        return getSettings(creditCardBoughtItemDTO.id,creditCardBoughtItemDTO.interest)?.let{setting->
            setting.takeIf { it.second > 0}?.let {
                creditCardBoughtItemDTO.settingCode = it.first
                creditCardBoughtItemDTO.settings = it.second
                Tax(KindOfTaxImpl.getNM(setting.second, KindOfTaxEnum.MONTHLY_EFFECTIVE),KindOfTaxEnum.MONTHLY_EFFECTIVE)
            }?: setting.takeIf{ it != null }?.let{
                creditCardBoughtItemDTO.settingCode = setting.first
                creditCardBoughtItemDTO.settings = setting.second
                Tax(setting.second, KindOfTaxEnum.MONTHLY_EFFECTIVE)
               }
        }?: when (creditCardBoughtItemDTO.kind) {
                TaxEnum.CASH_ADVANCE -> {
                    taxADVValue?.let {
                        Tax(KindOfTaxImpl.getNM(taxADVValue.value, taxADVValue.kind), KindOfTaxEnum.MONTHLY_EFFECTIVE)
                    }
                }
                TaxEnum.CREDIT_CARD -> {
                    taxCCValue?.let{
                        Tax(KindOfTaxImpl.getNM(taxCCValue.value, taxCCValue.kind), KindOfTaxEnum.MONTHLY_EFFECTIVE)
                    }
                }
                TaxEnum.WALLET_BUY->{
                    taxWBValue?.let{
                        Tax(KindOfTaxImpl.getNM(taxWBValue.value, taxWBValue.kind), KindOfTaxEnum.MONTHLY_EFFECTIVE)
                    }
                }
            }?: Tax(KindOfTaxImpl.getNM(creditCardBoughtItemDTO.interest, creditCardBoughtItemDTO.kindOfTax), KindOfTaxEnum.MONTHLY_EFFECTIVE)
    }

    private fun getSettings(codBought:Int,taxSetUp:Double):Pair<Int,Double>?{
        return buyCreditCardSettingSvc.get(codBought).also { Log.v(this.javaClass.name," response buy setting $it") }?.let{buyCCSettingDto ->
            creditCardSettingSvc.get(buyCCSettingDto.codeCreditCardSetting)?.let{creditCardSettingDto->
                Pair(creditCardSettingDto.id,creditCardSettingDto.value.toDouble()).also { Log.v(this.javaClass.name,"getSettings: $it") }
            }?:taxSetUp.takeIf { it == 0.0}?.let{
                creditCardSettingSvc.get(0).also { Log.v(this.javaClass.name," response setting $it") }?.let{creditCardSettingDto ->
                    Pair(creditCardSettingDto.id,creditCardSettingDto.value.toDouble()).also { Log.v(this.javaClass.name,"getSettings: $it") }
                }
            }
        }
    }

    private fun getQuotesBoughtTotal(dto:CreditCardBoughtItemDTO,cutOff:LocalDateTime):Long{
        return dto.month.takeIf {it > 1}?.let {
                    DateUtils.getMonths(dto.boughtDate,cutOff)
                }?:0
    }

    private fun getPendingToPay(dto:CreditCardBoughtItemDTO,differQuotes:List<DifferInstallmentDTO>):Double{
        return dto.month.takeIf {  it > 1}?.let{
            val differQuote = differQuotes.firstOrNull{ it.cdBoughtCreditCard.toInt() == dto.id }
            val capitalBought = dto.capitalValue * dto.monthPaid
            (differQuote?.let {
                it.pendingValuePayable - capitalBought
            }?:dto.valueItem.minus(capitalBought))
                .takeIf { it > 0.0 }?: 0.0
        }?: dto.valueItem
    }

    private fun getInterest(dto:CreditCardBoughtItemDTO,creditCard:CreditCard,differQuotes: List<DifferInstallmentDTO>):Double{
        return dto.month.takeIf { it > 1}?.let{
            val differQuote = differQuotes.firstOrNull{ it.cdBoughtCreditCard.toInt() == dto.id }
            creditCard.takeIf {
                creditCard.interest1NotQuote
                && dto.monthPaid == 0L
                && dto.kind == TaxEnum.CREDIT_CARD
                && differQuote == null}?.let{
                0.0
            }?: creditCard.takeIf {
                creditCard.interest1NotQuote
                && dto.monthPaid == 1L
                && dto.month > 1
                && dto.kind == TaxEnum.CREDIT_CARD}?.let{
                (dto.pendingToPay * dto.interest) + (dto.valueItem * dto.interest).also {
                    Log.v(javaClass.name,"=== getInterest: id ${dto.id}  ${dto.pendingToPay} X ${dto.interest} + ${dto.valueItem} X ${dto.interest} = ${dto.pendingToPay * dto.interest} + ${dto.valueItem * dto.interest} = $it")
                }
            }?:dto.pendingToPay.takeIf { it > 0.0}?.let {
                (dto.pendingToPay * dto.interest).also{
                    Log.v(this.javaClass.name,"=== getInterest: id ${dto.id} Value ${dto.valueItem} -  Pending ${dto.pendingToPay} interest ${dto.interest} ${dto.kindOfTax.getName()} = $it")
                }
            }
        } ?: 0.0
    }

}