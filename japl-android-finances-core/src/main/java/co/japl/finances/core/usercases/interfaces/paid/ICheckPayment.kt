package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import java.time.YearMonth

interface ICheckPayment {
    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

    fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO>

    fun update(check: CheckPaymentDTO): Boolean

    fun save(check: CheckPaymentDTO): CheckPaymentDTO

    fun delete(dto: CheckPaymentDTO): Boolean
}