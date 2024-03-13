package co.japl.android.finances.services.cache.impl

import co.japl.android.finances.services.cache.interfaces.IQuoteCreditCardCache
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Period
import javax.inject.Inject

class QuoteCreditCardCache @Inject constructor(private val quoteCreditCardSvc:IQuoteCreditCardDAO): IQuoteCreditCardCache {

    private val list = mutableListOf<CreditCardBoughtDTO>()

    override fun getToDate(
        codeCreditCard: Int,
        startDate: LocalDateTime,
        cutOffDate: LocalDateTime
    ): List<CreditCardBoughtDTO> {
        var cacheAble =  list.filter {
            it.codeCreditCard == codeCreditCard &&
                    it.boughtDate.isAfter(startDate) &&
                    it.boughtDate.isBefore(cutOffDate) &&
                    it.endDate.isAfter(startDate)
        }
        if(cacheAble.isEmpty()){
            quoteCreditCardSvc.getToDate(codeCreditCard,startDate,cutOffDate)
                .forEach {
                    list.add(it)
                    cacheAble = list
                }
        }
        return cacheAble
    }

    override fun get(codeBought: Int): CreditCardBoughtDTO? {
        var cacheAble:CreditCardBoughtDTO? = list.firstOrNull { it.id == codeBought }
        if(cacheAble == null){
            val bought = quoteCreditCardSvc.get(codeBought)
            if(bought.isPresent) {
                list.add(bought.get())
                cacheAble = bought.get()
            }
        }
        return cacheAble
    }

    override fun create(bought: CreditCardBoughtDTO): Int {
        if(list.any { bought.id == it.id }.not()) {
            bought.id = 10000+list.size
            list.add(bought)
            return 1
        }
        return 0
    }

    override fun update(bought: CreditCardBoughtDTO): Boolean {
        if(list.any{ bought.id == it.id }) {
            list.removeIf { it.id == bought.id }
            list.add(bought)
            return true
        }
        return false
    }

    override fun delete(key: Int): Boolean {
        if(list.any{ key == it.id }) {
            list.removeIf { it.id == key }
            return true
        }
        return false
    }

    override fun getInterestPendingQuotes(
        codeCreditCard: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): BigDecimal? {
        TODO("Not yet implemented")
    }

    override fun getPendingQuotes(
        codeCreditCard: Int,
        startDate: LocalDateTime,
        cutoffCurrent: LocalDateTime
    ): List<CreditCardBoughtDTO> {
        var cacheable = list.filter {
            it.codeCreditCard == codeCreditCard &&
                    it.month > 1 &&
                    it.boughtDate.isBefore(startDate) &&
                    Period.between(it.boughtDate.toLocalDate(),cutoffCurrent.toLocalDate()).months < it.month &&
                    it.recurrent == "0".toShort() &&
                    it.endDate.isAfter(startDate)
        }
        if(cacheable.isEmpty()){
            quoteCreditCardSvc.getPendingQuotes(codeCreditCard,startDate,cutoffCurrent)?.takeIf {
                it.isNotEmpty()
            }?.let {
                cacheable = it
                list.addAll(it)
            }
        }
        return cacheable
    }

    override fun getRecurrentPendingQuotes(
        codeCreditCard: Int,
        cutOff: LocalDateTime
    ): List<CreditCardBoughtDTO> {
        var cacheable = list.filter {
            it.codeCreditCard == codeCreditCard &&
                    it.month > 1 &&
                    it.boughtDate.isBefore(cutOff) &&
                    Period.between(it.boughtDate.toLocalDate(),cutOff.toLocalDate()).months < it.month &&
                    it.recurrent == "1".toShort() &&
                    it.endDate.isAfter(cutOff)
        }
        if(cacheable.isEmpty()){
            quoteCreditCardSvc.getRecurrentPendingQuotes(codeCreditCard,cutOff)?.takeIf {
                it.isNotEmpty()
            }?.let {
                cacheable = it
                list.addAll(it)
            }
        }
        return cacheable
    }
}