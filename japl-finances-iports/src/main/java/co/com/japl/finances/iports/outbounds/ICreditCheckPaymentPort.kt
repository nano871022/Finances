package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import java.time.YearMonth

interface ICreditCheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

    fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO>

    fun save(check: CheckPaymentDTO): CheckPaymentDTO

}