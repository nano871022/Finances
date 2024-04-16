package co.com.japl.finances.iports.inbounds.creditcard.bought

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import java.util.Optional

interface ICheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

}