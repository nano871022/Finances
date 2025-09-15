package co.com.japl.module.credit.views.fakes

import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort

class FakePeriodGracePort : IPeriodGracePort {
    override fun add(dto: GracePeriodDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(codeCredit: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasGracePeriod(codeCredit: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(codeCredit: Int): List<GracePeriodDTO> {
        TODO("Not yet implemented")
    }

}
