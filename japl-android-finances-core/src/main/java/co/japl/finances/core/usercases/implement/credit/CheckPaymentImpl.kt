package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.outbounds.ICreditCheckPaymentPort
import co.japl.finances.core.usercases.interfaces.credit.ICheckPayment
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:ICreditCheckPaymentPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }
}