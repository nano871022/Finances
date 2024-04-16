package co.japl.finances.core.usercases.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort
import co.japl.finances.core.usercases.interfaces.creditcard.ICheckPayment
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:ICreditCardCheckPaymentPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }
}