package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.PaidRecapDTO
import co.com.japl.finances.iports.outbounds.IPaidPort
import co.com.japl.finances.iports.outbounds.IPaidRecapPort
import co.japl.finances.core.usercases.interfaces.paid.IPaid
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class PaidImpl @Inject constructor(private val recapSvc: IPaidRecapPort,private val paidSvc:IPaidPort)   : IPaid {
    override fun get(codeAccount: Int, period: YearMonth): List<PaidDTO> {

        return paidSvc.getActivePaid(codeAccount, period)
    }

    override fun get(codePaid: Int): PaidDTO? {
        return paidSvc.get(codePaid)
    }

    override fun getRecap(codeAccount: Int, period: YearMonth): PaidRecapDTO? {
        return paidSvc.getActivePaid(codeAccount, period).takeIf { it.isNotEmpty() }?.let{
            PaidRecapDTO(
                date = period,
                count = it.size,
                totalPaid = it.map { it.itemValue }.sumOf{it}
            )
        }
    }

    override fun getListGraph(codeAccount: Int, period: YearMonth): List<Pair<String, Double>> {
        return get(codeAccount,period)?.takeIf { it.isNotEmpty() }?.let{
            it.groupBy { it.itemName }.map { Pair(it.key, it.value.sumOf { it.itemValue }) }
        }?: emptyList()
    }

    override fun create(paid: PaidDTO): Int {
        return paidSvc.create(paid)
    }

    override fun update(paid: PaidDTO): Boolean {
        return paidSvc.update(paid)
    }

    override fun delete(id: Int): Boolean {
        return paidSvc.delete(id)
    }

    override fun endRecurrent(id: Int): Boolean {
        return paidSvc.get(id)?.let {
            return paidSvc.update(it.copy(end = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).minusDays(1)))
        }?:false
    }

    override fun copy(id: Int): Boolean {
        return paidSvc.get(id)?.let {
            return paidSvc.create(it.copy(id = 0, itemName = it.itemName + "*"))>0
        }?:false
    }
}