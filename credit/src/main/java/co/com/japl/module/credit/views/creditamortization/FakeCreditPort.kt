package co.com.japl.module.credit.views.creditamortization

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import java.time.LocalDate

class FakeCreditPort : ICreditPort {
    override fun get(codeCredit: Long): List<CreditDTO> {
        return emptyList()
    }

    override fun get(codeCredit: Int): CreditDTO? {
        return CreditDTO(
            id = 1,
            name = "Test",
            date = LocalDate.now(),
            tax = 25.2,
            periods = 6,
            value = 10000.toBigDecimal(),
            quoteValue = 800.toBigDecimal(),
            kindOf = KindPaymentsEnums.ANNUAL,
            kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE
        )
    }

    override fun getCredits(): List<CreditDTO> {
        return emptyList()
    }

    override fun delete(codeCredit: Int): Boolean {
        return true
    }

    override fun save(credit: CreditDTO): Boolean {
        return true
    }
}
