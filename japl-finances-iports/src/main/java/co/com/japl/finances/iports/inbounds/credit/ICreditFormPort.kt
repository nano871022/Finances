package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import java.math.BigDecimal

interface ICreditFormPort {

    fun save(credit:CreditDTO):Int

    fun calculateQuoteCredit(value:BigDecimal,rate:Double,kindRate:KindOfTaxEnum,month:Int):BigDecimal

    fun findCreditById(id:Int):CreditDTO?
}