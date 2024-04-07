package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.PeriodPaidDTO

interface IPeriodPaid {
    fun get(codeAccount:Int):List<PeriodPaidDTO>
}