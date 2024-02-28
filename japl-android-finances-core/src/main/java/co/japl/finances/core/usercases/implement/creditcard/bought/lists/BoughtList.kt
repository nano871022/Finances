package co.japl.finances.core.usercases.implement.creditcard.bought.lists

import android.util.Log
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.usercases.calculations.InterestCalculations
import co.japl.finances.core.usercases.calculations.RecapCalculation
import co.japl.finances.core.usercases.implement.common.ListBoughts
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.utils.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

class BoughtList @Inject constructor(
    private val quoteCCSvc:IQuoteCreditCardPort
    ,private val recapCalculation: RecapCalculation
    ,private val differQuotesSvc:IDifferInstallmentRecapPort
    ,private val tagsSvc:ITagQuoteCreditCardPort
    , private val creditCardSvc:ICreditCardPort
    , private val interestCalculation: InterestCalculations
    , private val listBoughts: ListBoughts

): IBoughtList {

    override fun getBoughtList(creditCard:CreditCard,cutoff:LocalDateTime): CreditCardBoughtListDTO {
        val recap = RecapCreditCardBoughtListDTO()
        val period = YearMonth.of(cutoff.year,cutoff.month)
        val list = listBoughts.getList(creditCard,cutoff)
        val taxQuote = interestCalculation.getTax(creditCard.codeCreditCard,KindInterestRateEnum.CREDIT_CARD,period)
        val taxAdvance = interestCalculation.getTax(creditCard.codeCreditCard,KindInterestRateEnum.CASH_ADVANCE,period)
        val differQuotes = differQuotesSvc.get(cutoff.toLocalDate())

        list.map{
            listBoughts.calculateValues(it,creditCard,taxQuote,taxAdvance,cutoff,differQuotes)
        }.forEach { dto->
            tag(dto.id)?.let { dto.tagName = it.name }
            recapCalculation.getRecap(recap,dto)
        }
        return CreditCardBoughtListDTO(list,recap).also {
            Log.d(javaClass.name,"<<<=== GetBoughtList CreditCard Id: ${creditCard.codeCreditCard} Cutoff: ${creditCard.cutOff} Size: ${it.list?.size}")
        }
    }

    override fun delete(codeBought: Int): Boolean {
        require(codeBought > 0)
        return quoteCCSvc.delete(codeBought)
    }

    override fun endingRecurrentPayment(codeBought: Int, cutoff: LocalDateTime): Boolean {
        return quoteCCSvc.endingRecurrentPayment(codeBought,cutoff)
    }

    override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime): Boolean {
        quoteCCSvc.get(codeBought)?.let {
            val newRecurrent = it.copy(
                endDate = LocalDateTime.of(LocalDate.MAX, LocalTime.MAX)
                , createDate = cutOff
                , id = 0
                , valueItem = value.toBigDecimal()
                )
                newRecurrent.boughtDate = it.boughtDate.withYear(newRecurrent.createDate.year).withMonth(newRecurrent.createDate.monthValue)
            val origin = it.copy(endDate = cutOff.minusMonths(1).plusDays(1))
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

            val differBought =  it.copy(month = (value).toInt()
                , createDate = LocalDateTime.now()
                , endDate =  DateUtils.cutOffAddMonth(dayOfMonth, cutOff, value)
                , boughtDate =  LocalDateTime.now()
                , valueItem = (it.valueItem.toDouble() - ((it.valueItem.toDouble() / it.month) * months)).toBigDecimal()
                , nameItem = it.nameItem.plus(" (${it.id}. ${it.valueItem.toDouble()})")
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

    override fun clone(codeBought: Int): Boolean {
        require(codeBought > 0)
        return quoteCCSvc.get(codeBought)?.let {
             return quoteCCSvc.create(it.copy(id=0,
                 nameItem = it.nameItem + "*",
                 createDate = LocalDateTime.now(),
                 boughtDate = LocalDateTime.now())) > 0
        }?:false
    }


    private fun tag(codeBought:Int):TagDTO?{
        return tagsSvc.getTags(codeBought).firstOrNull()
    }
}