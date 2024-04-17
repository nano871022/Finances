package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.inbounds.paid.ICheckPaymentPort
import co.japl.finances.core.usercases.interfaces.paid.ICheckPayment
import java.time.YearMonth
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc: ICheckPayment):ICheckPaymentPort{
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        return svc.getCheckPayments(period)
    }
}