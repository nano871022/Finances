package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditMapper
import co.japl.android.finances.services.dao.interfaces.ICreditDAO
import co.com.japl.finances.iports.outbounds.ICreditFixRecapPort
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import co.com.japl.finances.iports.outbounds.ICreditPort
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class CreditFixImpl @Inject constructor(private val creditFix: ICreditDAO):ICreditFixRecapPort, ICreditPort {
    override fun getAll(): List<CreditDTO> {
        return creditFix.getAll().map (CreditMapper::mapper)
    }

    override fun getById(id: Int): CreditDTO? {
        return creditFix.get(id).takeIf {  it.isPresent }?.let { CreditMapper.mapper(it.get()) }
    }

    override fun getAllActive(period: YearMonth): List<CreditDTO> {
        return creditFix.getCurrentBoughtCredits(LocalDate.of(period.year,period.monthValue,1).plusMonths(1).minusDays(1))
            .map(CreditMapper::mapper)
    }

    override fun delete(id: Int): Boolean {
        return creditFix.delete(id)
    }

}