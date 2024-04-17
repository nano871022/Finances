package co.japl.finances.core.adapters.inbound.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.ICheckPaymentPort
import co.japl.finances.core.usercases.interfaces.creditcard.ICheckPayment
import java.time.YearMonth
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val service: ICheckPayment): ICheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return service.getPeriodsPayment()
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        return service.getCheckPayments(period)
    }


}