package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditCardBoughtMapper
import co.japl.android.finances.services.interfaces.IQuoteCreditCardSvc
import co.japl.finances.core.adapters.outbound.interfaces.IQuoteCreditCardPort
import co.japl.finances.core.dto.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

class QuoteCreditCardImpl @Inject constructor(private val quoteCCSvc: IQuoteCreditCardSvc): IQuoteCreditCardPort {
    override fun getRecurrentBuys(key: Int, cutOff: LocalDateTime): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getRecurrentBuys(key,cutOff).map (CreditCardBoughtMapper::mapper)
    }

    override fun getToDate(
        key: Int,
        startDate: LocalDateTime,
        cutOffDate: LocalDateTime
    ): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getToDate(key,startDate,cutOffDate).map (CreditCardBoughtMapper::mapper)
    }

    override fun getCapitalPendingQuotes(
        idCreditCard: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): BigDecimal? {
        return quoteCCSvc.getCapitalPendingQuotes(idCreditCard,startDate,endDate).orElse(BigDecimal.ZERO)
    }

    override fun getInterestPendingQuotes(
        idCreditCard: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): BigDecimal? {
        return quoteCCSvc.getInterestPendingQuotes(idCreditCard,startDate,endDate).orElse(BigDecimal.ZERO)
    }

    override fun getPendingQuotes(
        key: Int,
        startDate: LocalDateTime,
        cutoffCurrent: LocalDateTime
    ): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getPendingQuotes(key,startDate,cutoffCurrent).map (CreditCardBoughtMapper::mapper)
    }

    override fun getRecurrentPendingQuotes(
        key: Int,
        cutOff: LocalDateTime
    ): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getRecurrentPendingQuotes(key, cutOff).map(CreditCardBoughtMapper::mapper)
    }

}