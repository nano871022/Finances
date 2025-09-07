package co.com.japl.module.credit.views.creditamortization

import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort

class FakePeriodGracePort : IPeriodGracePort {
    override fun get(id: Int): List<GracePeriodDTO> {
        return emptyList()
    }

    override fun create(gracePeriod: GracePeriodDTO): Boolean {
        return true
    }

    override fun update(gracePeriod: GracePeriodDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }
}
