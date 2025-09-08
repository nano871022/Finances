package co.com.japl.module.credit.views.forms

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import java.math.BigDecimal

class FakeCreditFormSvc : ICreditFormPort {
    override fun save(credit: CreditDTO): Int {
        TODO("Not yet implemented")
    }

    override fun calculateQuoteCredit(
        value: BigDecimal,
        rate: Double,
        kindRate: KindOfTaxEnum,
        month: Int
    ): BigDecimal {
        TODO("Not yet implemented")
    }

    override fun findCreditById(id: Int): CreditDTO? {
        TODO("Not yet implemented")
    }

}
