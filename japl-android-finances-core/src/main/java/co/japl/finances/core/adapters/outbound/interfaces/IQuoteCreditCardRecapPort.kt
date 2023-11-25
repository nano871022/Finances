package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCardPort {

    fun getRecurrentBuys(key:Int,cutOff:LocalDateTime):List<CreditCardBoughtDTO>

    fun getToDate(key:Int,startDate:LocalDateTime,cutOffDate: LocalDateTime): List<CreditCardBoughtDTO>

    fun getCapitalPendingQuotes(idCreditCard:Int, startDate: LocalDateTime, endDate:LocalDateTime): BigDecimal?

    fun getInterestPendingQuotes(idCreditCard:Int, startDate: LocalDateTime, endDate:LocalDateTime): BigDecimal?

    fun getPendingQuotes(key:Int,startDate: LocalDateTime,cutoffCurrent: LocalDateTime): List<CreditCardBoughtDTO>

    fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>



}