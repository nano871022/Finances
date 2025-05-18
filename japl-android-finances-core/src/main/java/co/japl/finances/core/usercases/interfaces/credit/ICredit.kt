package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import java.time.YearMonth

interface ICredit {
    fun getAllEnable(period:YearMonth):List<CreditDTO>

    fun delete(id:Int):Boolean

    fun getCreditsEnables(period:YearMonth): List<RecapCreditDTO>

}