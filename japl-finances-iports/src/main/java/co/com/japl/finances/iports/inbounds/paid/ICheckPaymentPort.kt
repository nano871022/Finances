package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import java.util.Optional

interface ICheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

}