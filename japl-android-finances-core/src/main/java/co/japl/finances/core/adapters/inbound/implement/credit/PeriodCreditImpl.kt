package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort
import co.japl.finances.core.usercases.interfaces.credit.IPeriodCredit
import javax.inject.Inject


class PeriodCreditImpl @Inject constructor(private val periodSvc: IPeriodCredit) : IPeriodCreditPort{

    override fun getRecords(): List<PeriodCreditDTO> {
        return periodSvc.getRecords()
    }

}