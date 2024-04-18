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
        require(paid.id == 0) { "Code should be 0" }
        return paidSvc.create(paid)
    }

    override fun update(paid: PaidDTO): Boolean {
        require(paid.id > 0) { "Id cannot be 0" }
        require(paid.account > 0) { "Account cannot be 0" }
        return paidSvc.update(paid)
    }

    override fun delete(id: Int): Boolean {
        require(id > 0) { "Id cannot be 0" }
        return paidSvc.delete(id)
    }

    override fun endRecurrent(id: Int): Boolean {
        require(id > 0) { "Id cannot be 0" }
        return paidSvc.endRecurrent(id)
    }

    override fun copy(id: Int): Boolean {
        require(id > 0) { "Id cannot be 0" }
        return paidSvc.copy(id)
    }
}