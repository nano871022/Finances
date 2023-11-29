package co.japl.finances.core.usercases.interfaces.common

import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCard {

    fun getInterestPendingQuotes(codCreditCard: Int,startDate: LocalDateTime, cutOff: LocalDateTime): BigDecimal?

    fun getTotalQuote(): BigDecimal

    fun getCapital(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime): BigDecimal

    fun getCapitalPendingQuotes(key: Int,startDate: LocalDateTime, cutOff: LocalDateTime): BigDecimal

    fun getInterest(codCreditCard: Int, startDate:LocalDateTime,cutOff: LocalDateTime): BigDecimal

    fun getDataToGraphStats(
        codCreditCard: Int,
        cutOff: LocalDateTime
    ): List<Pair<String, Double>>

}