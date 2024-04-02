package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.outbounds.IPaidPort
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.com.japl.finances.iports.outbounds.IPaidRecapPort
import co.com.japl.finances.iports.outbounds.IPeriodPaidPort
import co.japl.android.finances.services.core.mapper.PaidMapper
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

class PaidImpl @Inject constructor(private val paidImpl: IPaidDAO) : IPaidRecapPort , IPaidPort, IPeriodPaidPort{
    override fun getTotalPaid(): BigDecimal {
        return paidImpl.getTotalPaid()
    }

    override fun getActivePaid(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        val items =  paidImpl.getAll(codeAccount,period).takeIf { it.isNotEmpty() }?.map { PaidMapper.mapper(it)}?: emptyList()
        val recurrents = paidImpl.getRecurrents(codeAccount,period).takeIf { it.isNotEmpty() }?.map { PaidMapper.mapper(it)}?: emptyList()
        val list =  mutableListOf<PaidDTO>()
        list.addAll(items)
        list.addAll(recurrents)
        return list
    }

    override fun get(codePaid: Int): PaidDTO? {
        return paidImpl.get(codePaid).takeIf { it.isPresent }?.get()?.let{PaidMapper.mapper(it)}
    }

    override fun create(paid: PaidDTO): Int {
        return paidImpl.save(PaidMapper.mapper(paid)).toInt()
    }

    override fun update(paid: PaidDTO): Boolean {
        return paidImpl.save(PaidMapper.mapper(paid)) > 0
    }

    override fun delete(codePaid: Int): Boolean {
        return paidImpl.delete(codePaid)
    }

    override fun get(): List<PeriodPaidDTO> {
        return paidImpl.getPeriods().takeIf { it.isNotEmpty() }?.map {
            PeriodPaidDTO(
                date = YearMonth.of( it.date.year, it.date.monthValue),
                value = it.value.toDouble(),
                count = it.id
            )
        }?: emptyList()
    }

}