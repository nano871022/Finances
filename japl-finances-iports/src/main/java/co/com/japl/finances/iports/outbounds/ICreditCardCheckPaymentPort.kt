package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO

interface ICreditCardCheckPaymentPort {

    fun getPeriodsPayment():List<PeriodCheckPaymentDTO>

}