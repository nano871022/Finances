package co.com.japl.finances.iports.inbounds.creditcard.bought

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import java.time.YearMonth
import java.util.Optional

interface ICheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

    fun getCheckPayments(period: YearMonth):List<CheckPaymentDTO>

}