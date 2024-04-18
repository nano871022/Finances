package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import java.time.YearMonth
import java.util.Optional

interface ICheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

    fun getCheckPayments(period:YearMonth):List<CheckPaymentDTO>

    fun update(check: CheckPaymentDTO): Boolean

    fun save(check: CheckPaymentDTO): CheckPaymentDTO

}