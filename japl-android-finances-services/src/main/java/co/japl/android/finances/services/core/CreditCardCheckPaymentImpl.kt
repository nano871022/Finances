package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort
import co.japl.android.finances.services.core.mapper.CheckPaymentMapper
import co.japl.android.finances.services.dao.interfaces.ICheckQuoteDAO
import javax.inject.Inject

class CreditCardCheckPaymentImpl @Inject constructor(private val svc:ICheckQuoteDAO): ICreditCardCheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment().map(CheckPaymentMapper::mapper)
    }
}