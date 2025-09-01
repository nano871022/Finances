package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import java.math.BigDecimal
import javax.inject.Inject

class CreditFormImpl @Inject constructor(val svc:ICredit) : ICreditFormPort {
    override fun save(credit: CreditDTO): Int {
        return svc.save(credit)
    }

    override fun calculateQuoteCredit(
        value: BigDecimal,
        rate: Double,
        kindRate: KindOfTaxEnum,
        month: Int
    ): BigDecimal {
        return svc.calculateQuoteCredit(value, rate, kindRate, month)
    }

    override fun findCreditById(id: Int): CreditDTO? {
        return svc.findCreditById(id)
    }

}