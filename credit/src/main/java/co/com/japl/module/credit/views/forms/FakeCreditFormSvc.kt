package co.com.japl.module.credit.views.forms

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import java.math.BigDecimal

class FakeCreditFormSvc : ICreditFormPort {
    override fun findCreditById(id: Int): CreditDTO? {
        return null
    }

    override fun save(dto: CreditDTO): Int {
        return 1
    }

    override fun calculateQuoteCredit(
        value: BigDecimal,
        rate: Double,
        month: Int,
        kindRate: KindOfTaxEnum
    ): BigDecimal {
        return BigDecimal.ZERO
    }
}
