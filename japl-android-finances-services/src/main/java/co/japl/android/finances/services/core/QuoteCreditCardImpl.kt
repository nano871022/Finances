package co.japl.android.finances.services.core

import android.database.CursorWindowAllocationException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.core.mapper.CreditCardBoughtMapper
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.japl.android.finances.services.cache.interfaces.IQuoteCreditCardCache
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.japl.android.finances.services.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Period
import java.time.YearMonth
import javax.inject.Inject

class QuoteCreditCardImpl @Inject constructor(private val quoteCCSvc: IQuoteCreditCardDAO,
                                              private val creditcardSvc:ICreditCardSvc,
                                              private val quoteCreditCardCache:IQuoteCreditCardCache): IQuoteCreditCardPort {

    val EXPREG_DIFFER = "\\((\\d{1,4}\\.) (\\d*\\.\\d{1,2})\\)".toRegex()
    override fun getRecurrentBuys(key: Int, cutOff: LocalDateTime): List<CreditCardBoughtDTO> {
        return quoteCCSvc.getRecurrentBuys(key,cutOff).map (CreditCardBoughtMapper::mapper)
    }

    override fun getToDate(
        key: Int,
        startDate: LocalDateTime,
        cutOffDate: LocalDateTime,
        cache: Boolean
    ): List<CreditCardBoughtDTO> {
        val result = if(cache.not()){quoteCCSvc.getToDate(key,startDate,cutOffDate).map (CreditCardBoughtMapper::mapper)}
        else{quoteCreditCardCache.getToDate(key,startDate,cutOffDate).map (CreditCardBoughtMapper::mapper)}
        val list = result.filter{
            !EXPREG_DIFFER.containsMatchIn(it.nameItem)
        }.toMutableList()

        result.filter {
            EXPREG_DIFFER.containsMatchIn(it.nameItem)
        }.map{
            EXPREG_DIFFER.find(it.nameItem)?.let{match->
                val id = match.groupValues[1]?.replace(".", "")?.toInt() ?: 0
                quoteCCSvc.get(id).takeIf { it.isPresent }?.let {find->
                    val months = Period.between(find.get().boughtDate.toLocalDate(),it.boughtDate.toLocalDate()).toTotalMonths()
                    Log.d(javaClass.name,"Month ${it.month} ${find.get().month} differ: $months ${it.boughtDate} ${find.get().boughtDate}")
                     it.boughtDate = find.get().boughtDate
                    it.month += months.toInt()
                    list.add(it)
                }
            }
        }
        return list
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
        endDate: LocalDateTime,
        cache: Boolean
    ): BigDecimal? {
        return if(cache.not()){quoteCCSvc.getInterestPendingQuotes(idCreditCard,startDate,endDate).orElse(BigDecimal.ZERO)}
        else{quoteCreditCardCache.getInterestPendingQuotes(idCreditCard,startDate,endDate)?:BigDecimal.ZERO}
    }

    override fun getPendingQuotes(
        key: Int,
        startDate: LocalDateTime,
        cutoffCurrent: LocalDateTime
        ,cache:Boolean
    ): List<CreditCardBoughtDTO> {
        return if(cache.not()){quoteCCSvc.getPendingQuotes(key,startDate,cutoffCurrent).map (CreditCardBoughtMapper::mapper)}
        else{quoteCreditCardCache.getPendingQuotes(key,startDate,cutoffCurrent).map (CreditCardBoughtMapper::mapper)}
    }

    override fun getRecurrentPendingQuotes(
        key: Int,
        cutOff: LocalDateTime
        ,cache:Boolean
    ): List<CreditCardBoughtDTO> {
        return if(cache.not()){quoteCCSvc.getRecurrentPendingQuotes(key, cutOff).map(CreditCardBoughtMapper::mapper)}
        else{quoteCreditCardCache.getRecurrentPendingQuotes(key, cutOff).map(CreditCardBoughtMapper::mapper)}
    }

    override fun get(codeBought: Int,cache:Boolean): CreditCardBoughtDTO? {
        return if(cache.not()){quoteCCSvc.get(codeBought).takeIf { it.isPresent }?.map(CreditCardBoughtMapper::mapper)?.get()}
        else{quoteCreditCardCache.get(codeBought)?.let(CreditCardBoughtMapper::mapper)}
    }

    override fun delete(key: Int,cache:Boolean): Boolean {
        return quoteCCSvc.delete(key)
    }

    override fun endingRecurrentPayment(key: Int, cutOff: LocalDateTime): Boolean {
        return quoteCCSvc.endingRecurrentPayment(key,cutOff)
    }

    override fun create(bought: CreditCardBoughtDTO,cache:Boolean): Int {
        require(bought.id == 0)
        return if(cache.not()){quoteCCSvc.save(CreditCardBoughtMapper.mapper(bought)).toInt()}
        else{quoteCreditCardCache.create(CreditCardBoughtMapper.mapper(bought))}
    }

    override fun update(bought: CreditCardBoughtDTO,cache:Boolean): Boolean {
        require(bought.id > 0)
        return if(cache.not()){quoteCCSvc.save(CreditCardBoughtMapper.mapper(bought)).toInt() > 0}
        else{quoteCreditCardCache.update(CreditCardBoughtMapper.mapper(bought))}
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun getBoughtPeriodList(idCreditCard: Int): List<LocalDateTime>? {
        try {
            val list = quoteCCSvc.getPeriod(idCreditCard)
            val cutOffDay = creditcardSvc.get(idCreditCard).get().cutOffDay
            Log.d(javaClass.name,"=== GetBoughtPeriodList: CutOffDay: $cutOffDay")
            return list?.map { DateUtils.cutoffDate(cutOffDay, it.month.value.toShort(),it.year ) }
        }catch(e: CursorWindowAllocationException){
            Log.d(javaClass.name,"Error: ${e.message}")
        }
        return null
    }

    override fun findByNameAndBoughtDateAndValue(
        name: String,
        boughtDate: LocalDateTime,
        amount: BigDecimal
    ): CreditCardBoughtDTO? {
        return quoteCCSvc.findByNameAndBoughtDateAndValue(name,boughtDate,amount)?.let(CreditCardBoughtMapper::mapper)
    }

}