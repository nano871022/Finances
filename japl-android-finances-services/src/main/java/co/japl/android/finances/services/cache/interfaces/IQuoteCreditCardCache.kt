package co.japl.android.finances.services.cache.interfaces

import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCardCache {

    fun getToDate(
        key: Int,
        startDate: LocalDateTime,
        cutOffDate: LocalDateTime
    ): List<CreditCardBoughtDTO>

    fun get(codeBought: Int): CreditCardBoughtDTO?

    fun create(bought: CreditCardBoughtDTO): Int

    fun update(bought: CreditCardBoughtDTO): Boolean

    fun delete(key: Int): Boolean
    fun getInterestPendingQuotes(
        idCreditCard: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime): BigDecimal?

    fun getPendingQuotes(
    key: Int,
    startDate: LocalDateTime,
    cutoffCurrent: LocalDateTime): List<CreditCardBoughtDTO>

    fun getRecurrentPendingQuotes(
        key: Int,
        cutOff: LocalDateTime): List<CreditCardBoughtDTO>

}