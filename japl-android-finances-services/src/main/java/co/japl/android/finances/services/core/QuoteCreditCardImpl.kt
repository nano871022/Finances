package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditCardBoughtMapper
import co.japl.android.finances.services.interfaces.IQuoteCreditCardSvc
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Optional
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

    override fun get(codeBought: Int): CreditCardBoughtDTO? {
        return quoteCCSvc.get(codeBought).takeIf { it.isPresent }?.map(CreditCardBoughtMapper::mapper)?.get()
    }

    override fun delete(key: Int): Boolean {
        return quoteCCSvc.delete(key)
    }

    override fun endingRecurrentPayment(key: Int, cutOff: LocalDateTime): Boolean {
        return quoteCCSvc.endingRecurrentPayment(key,cutOff)
    }

    override fun create(bought: CreditCardBoughtDTO): Int {
        require(bought.id == 0)
        return quoteCCSvc.save(CreditCardBoughtMapper.mapper(bought)).toInt()
    }

    override fun update(bought: CreditCardBoughtDTO): Boolean {
        require(bought.id > 0)
        return quoteCCSvc.save(CreditCardBoughtMapper.mapper(bought)).toInt() > 0
    }

}