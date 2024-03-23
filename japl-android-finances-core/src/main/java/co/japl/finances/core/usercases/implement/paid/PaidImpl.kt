package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.PaidRecapDTO
import co.com.japl.finances.iports.outbounds.IPaidRecapPort
import co.japl.finances.core.usercases.interfaces.paid.IPaid
import java.time.YearMonth
import javax.inject.Inject

class PaidImpl @Inject constructor(private val recapSvc: IPaidRecapPort)   : IPaid {
    override fun get(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        return recapSvc.getActivePaid(codeAccount, period)
    }

    override fun get(codePaid: Int): PaidDTO? {
        return recapSvc.get(codePaid)
    }

    override fun getRecap(codeAccount: Int, period: YearMonth): PaidRecapDTO? {
        return recapSvc.getActivePaid(codeAccount, period).takeIf { it.isNotEmpty() }?.let{
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
        return recapSvc.create(paid)
    }

    override fun update(paid: PaidDTO): Boolean {
        return recapSvc.update(paid)
    }
}