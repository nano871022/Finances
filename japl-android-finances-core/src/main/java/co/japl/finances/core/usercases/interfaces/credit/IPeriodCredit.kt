package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.PeriodCreditDTO

interface IPeriodCredit {
    fun getRecords(): List<PeriodCreditDTO>
}