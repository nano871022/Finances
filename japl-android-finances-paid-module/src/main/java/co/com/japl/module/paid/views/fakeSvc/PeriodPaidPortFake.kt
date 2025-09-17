package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort

class PeriodPaidPortFake : IPeriodPaidPort {
    override fun get(codeAccount: Int): List<PeriodPaidDTO> {
        return emptyList()
    }
}
