package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.outbounds.IPeriodPaidPort
import co.japl.finances.core.usercases.interfaces.paid.IPeriodPaid
import javax.inject.Inject

class PeriodPaid @Inject constructor(private val periodPaidSvc: IPeriodPaidPort) : IPeriodPaid {
    override fun get(codeAccount: Int): List<PeriodPaidDTO> {
        return periodPaidSvc.get(codeAccount.toLong())
    }
}