package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO

interface ICheckPayment {
    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>
}