package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.PeriodPaidDTO

interface IPeriodPaidPort {

    fun get():List<PeriodPaidDTO>

}