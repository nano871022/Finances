package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO

interface ICreditCheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

}