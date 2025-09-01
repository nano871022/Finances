package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.PeriodCreditDTO

interface IPeriodCreditPort {

    fun getRecords():List<PeriodCreditDTO>

}