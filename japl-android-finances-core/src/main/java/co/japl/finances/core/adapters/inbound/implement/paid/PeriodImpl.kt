package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.japl.finances.core.usercases.interfaces.paid.IPeriodPaid
import javax.inject.Inject

class PeriodImpl @Inject constructor(private val periodPaidSvc:IPeriodPaid): IPeriodPaidPort {
    override fun get(): List<PeriodPaidDTO> {
        return periodPaidSvc.get()
    }
}