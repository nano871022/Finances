package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCard {

    fun getInterestPendingQuotes(codCreditCard: Int,startDate: LocalDateTime, cutOff: LocalDateTime,cache:Boolean): BigDecimal?

    fun getTotalQuote(cache:Boolean): BigDecimal

    fun getCapital(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime,cache:Boolean): BigDecimal

    fun getCapitalPendingQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime,cache:Boolean): BigDecimal

    fun getInterest(codCreditCard: Int, startDate:LocalDateTime,cutOff: LocalDateTime,cache:Boolean): BigDecimal

    fun getDataToGraphStats(
        codCreditCard: Int,
        cutOff: LocalDateTime
        ,cache:Boolean
    ): List<Pair<String, Double>>

}