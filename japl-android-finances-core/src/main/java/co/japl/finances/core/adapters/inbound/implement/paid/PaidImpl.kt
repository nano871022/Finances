package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.PaidRecapDTO
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.japl.finances.core.usercases.interfaces.paid.IPaid
import java.time.YearMonth
import javax.inject.Inject

class PaidImpl @Inject constructor(private val paidSvc:IPaid): IPaidPort {
    override fun get(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        return paidSvc.get(codeAccount,period)
    }

    override fun get(codePaid: Int): PaidDTO? {
        return paidSvc.get(codePaid)
    }

    override fun getRecap(codeAccount: Int, period: YearMonth): PaidRecapDTO? {
        return paidSvc.getRecap(codeAccount,period)
    }

    override fun getListGraph(codeAccount: Int, period: YearMonth): List<Pair<String, Double>> {
        return paidSvc.getListGraph(codeAccount,period)
    }

    override fun create(paid: PaidDTO): Int {
        return paidSvc.create(paid)
    }

    override fun update(paid: PaidDTO): Boolean {
        return paidSvc.update(paid)
    }
}