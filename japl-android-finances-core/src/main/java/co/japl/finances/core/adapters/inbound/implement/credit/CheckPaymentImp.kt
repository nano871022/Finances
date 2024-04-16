package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.inbounds.credit.ICheckPaymentPort
import co.japl.finances.core.usercases.interfaces.credit.ICheckPayment
import javax.inject.Inject

class CheckPaymentImp @Inject constructor(private val svc:ICheckPayment) : ICheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }
}