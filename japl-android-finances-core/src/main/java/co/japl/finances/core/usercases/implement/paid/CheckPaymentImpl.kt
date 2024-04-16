package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.outbounds.IPaidCheckPaymentPort
import co.japl.finances.core.usercases.interfaces.paid.ICheckPayment
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:IPaidCheckPaymentPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }
}