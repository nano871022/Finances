package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCardPort {

    fun getRecurrentBuys(key:Int,cutOff:LocalDateTime):List<CreditCardBoughtDTO>

    fun getToDate(key:Int,startDate:LocalDateTime,cutOffDate: LocalDateTime): List<CreditCardBoughtDTO>

    fun getCapitalPendingQuotes(idCreditCard:Int, startDate: LocalDateTime, endDate:LocalDateTime): BigDecimal?

    fun getInterestPendingQuotes(idCreditCard:Int, startDate: LocalDateTime, endDate:LocalDateTime): BigDecimal?

    fun getPendingQuotes(key:Int,startDate: LocalDateTime,cutoffCurrent: LocalDateTime): List<CreditCardBoughtDTO>

    fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>

    fun get(codeBought:Int):CreditCardBoughtDTO?

    fun delete(key:Int):Boolean

    fun endingRecurrentPayment(key:Int,cutOff:LocalDateTime):Boolean

    fun create(bought:CreditCardBoughtDTO):Int

    fun update(bought:CreditCardBoughtDTO):Boolean

    fun getBoughtPeriodList(idCreditCard: Int): List<LocalDateTime>?

}