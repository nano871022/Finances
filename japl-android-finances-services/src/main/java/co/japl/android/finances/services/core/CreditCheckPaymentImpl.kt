package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.outbounds.ICreditCheckPaymentPort
import co.japl.android.finances.services.core.mapper.CheckPaymentMapper
import co.japl.android.finances.services.dao.interfaces.ICheckCreditDAO
import javax.inject.Inject

class CreditCheckPaymentImpl @Inject constructor(private val svc:ICheckCreditDAO) : ICreditCheckPaymentPort {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment().map(CheckPaymentMapper::mapper)
    }
}