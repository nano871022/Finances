package co.japl.android.finances.services.core

import android.util.Log
import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.outbounds.IPeriodGracePort
import co.japl.android.finances.services.core.mapper.GracePeriodMapper
import co.japl.android.finances.services.dao.interfaces.IGracePeriodDAO
import java.time.LocalDate
import javax.inject.Inject

class PeriodGraceImpl @Inject constructor(private val dao:IGracePeriodDAO) : IPeriodGracePort {
    override fun add(dto: GracePeriodDTO): Boolean {
        val entity = GracePeriodMapper.mapper(dto)
        return dao.save(entity) > 0
    }

    override fun delete(codeCredit: Int): Boolean {
        return dao.get(codeCredit.toLong()).takeIf { it.isNotEmpty() }?.let{
            it.sortedByDescending { it.end }.first().let {
               return dao.delete(it.id)
            }
        }?:false
    }

    override fun hasGracePeriod(codeCredit: Int): Boolean {
        val currentDate = LocalDate.now()
        return dao.get(codeCredit.toLong()).any{
            currentDate.isAfter(it.create.withDayOfMonth(1)) && currentDate.isBefore(it.end.plusMonths(1).withDayOfMonth(1).minusDays(1))
        }
    }
}