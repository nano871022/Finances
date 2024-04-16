package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO

interface IPaidCheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

}