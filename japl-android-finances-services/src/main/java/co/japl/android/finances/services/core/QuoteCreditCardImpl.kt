package co.japl.android.finances.services.core

import android.database.CursorWindowAllocationException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.japl.android.finances.services.core.mapper.CreditCardBoughtMapper
import co.japl.android.finances.services.interfaces.IQuoteCreditCardSvc
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.japl.android.finances.services.core.mapper.BoughtCreditCardPeriodMapper
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Optional
import javax.inject.Inject

class QuoteCreditCardImpl @Inject constructor(private val quoteCCSvc: IQuoteCreditCardSvc, private val creditcardSvc:ICreditCardSvc): IQuoteCreditCardPort {
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun getBoughtPeriodList(idCreditCard: Int): List<LocalDateTime>? {
        try {
            val list = quoteCCSvc.getPeriod(idCreditCard)
            val cutOffDay = creditcardSvc.get(idCreditCard).get().cutOffDay
            return list?.map { DateUtils.startDateFromCutoff(cutOffDay, LocalDateTime.of(LocalDate.of(it.year, it.month,1),LocalTime.MIN)) }
        }catch(e: CursorWindowAllocationException){
            Log.d(javaClass.name,"Error: ${e.message}")
        }
        return null
    }

}