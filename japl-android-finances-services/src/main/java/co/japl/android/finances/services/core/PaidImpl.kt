package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.PaidDTO
import co.japl.android.finances.services.dao.interfaces.IPaidDAO
import co.com.japl.finances.iports.outbounds.IPaidRecapPort
import co.japl.android.finances.services.core.mapper.PaidMapper
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

class PaidImpl @Inject constructor(private val paidImpl: IPaidDAO) : IPaidRecapPort {
    override fun getTotalPaid(): BigDecimal {
        return paidImpl.getTotalPaid()
    }

    override fun getActivePaid(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        return  paidImpl.getAll(codeAccount,period).takeIf { it.isNotEmpty() }?.map { PaidMapper.mapper(it)}?: emptyList()
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

}