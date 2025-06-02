package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import java.math.BigDecimal
import java.time.YearMonth

interface ICredit {
    fun getAllEnable(period:YearMonth):List<CreditDTO>

    fun delete(id:Int):Boolean

    fun getCreditsEnables(period:YearMonth): List<RecapCreditDTO>

    fun save(credit:CreditDTO):Int

    fun calculateQuoteCredit(value: BigDecimal, rate:Double, kindRate: KindOfTaxEnum, month:Int): BigDecimal

    fun findCreditById(id:Int):CreditDTO?

}