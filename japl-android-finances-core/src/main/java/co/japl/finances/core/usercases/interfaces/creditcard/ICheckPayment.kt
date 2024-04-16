package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO

interface ICheckPayment {
    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>
}