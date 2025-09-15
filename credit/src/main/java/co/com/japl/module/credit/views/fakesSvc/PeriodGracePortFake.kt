package co.com.japl.module.credit.views.fakesSvc

import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import java.time.LocalDate

class PeriodGracePortFake : IPeriodGracePort {
    override fun get(id: Int): List<GracePeriodDTO> {
        return emptyList()
    }

    override fun add(dto: GracePeriodDTO): Boolean {
        return true
    }

    override fun update(dto: GracePeriodDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun hasGracePeriod(id: Int): Boolean {
        return false
    }
}
