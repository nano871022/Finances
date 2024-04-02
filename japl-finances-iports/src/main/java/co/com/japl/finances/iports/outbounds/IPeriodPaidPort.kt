package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PeriodPaidDTO

interface IPeriodPaidPort {

    fun get(): List<PeriodPaidDTO>
}