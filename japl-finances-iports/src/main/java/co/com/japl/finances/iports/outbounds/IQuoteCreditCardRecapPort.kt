package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCardPort {

    fun getRecurrentBuys(key:Int,cutOff:LocalDateTime):List<CreditCardBoughtDTO>

    fun getToDate(key:Int,startDate:LocalDateTime,cutOffDate: LocalDateTime,cache:Boolean): List<CreditCardBoughtDTO>

    fun getCapitalPendingQuotes(idCreditCard:Int, startDate: LocalDateTime, endDate:LocalDateTime): BigDecimal?

    fun getInterestPendingQuotes(idCreditCard:Int, startDate: LocalDateTime, endDate:LocalDateTime,cache:Boolean): BigDecimal?

    fun getPendingQuotes(key:Int,startDate: LocalDateTime,cutoffCurrent: LocalDateTime,cache:Boolean): List<CreditCardBoughtDTO>

    fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime,cache:Boolean):List<CreditCardBoughtDTO>

    fun get(codeBought:Int,cache:Boolean):CreditCardBoughtDTO?

    fun delete(key:Int,cache:Boolean):Boolean

    fun endingRecurrentPayment(key:Int,cutOff:LocalDateTime):Boolean

    fun endingPayment(key:Int,message:String,cutOff:LocalDateTime):Boolean

    fun create(bought:CreditCardBoughtDTO,cache:Boolean):Int

    fun update(bought:CreditCardBoughtDTO,cache:Boolean):Boolean

    fun getBoughtPeriodList(idCreditCard: Int): List<LocalDateTime>?

    fun findByNameAndBoughtDateAndValue(name:String,boughtDate:LocalDateTime,amount:BigDecimal):CreditCardBoughtDTO?

}