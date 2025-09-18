package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.PaidRecapDTO
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import java.time.YearMonth

class PaidPortFake : IPaidPort {
    override fun get(codeAccount: Int, period: YearMonth): List<PaidDTO> {
        return emptyList()
    }

    override fun getRecap(codeAccount: Int, period: YearMonth): PaidRecapDTO? {
        return null
    }

    override fun getListGraph(codeAccount: Int, period: YearMonth): List<Pair<String, Double>> {
        return emptyList()
    }

    override fun get(codePaid: Int): PaidDTO? {
        return null
    }

    override fun create(paid: PaidDTO): Int {
        return 1
    }

    override fun update(paid: PaidDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun endRecurrent(id: Int): Boolean {
        return true
    }

    override fun copy(id: Int): Boolean {
        return true
    }
}
