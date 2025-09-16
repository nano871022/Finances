package co.com.japl.module.credit.views.fakes

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort

class FakeAmortizationTablePort : IAmortizationTablePort {
    override fun getAmortization(
        codeCredit: Int,
        kind: KindAmortization,
        cache: Boolean
    ): List<AmortizationRowDTO> {
        return listOf(
            AmortizationRowDTO(
                id = 1,
                periods = 6,
                creditRate = 25.5,
                kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
                creditValue = 10_000.toBigDecimal(),
                amortizatedValue = 10_000.toBigDecimal(),
                capitalValue = 800.toBigDecimal(),
                interestValue = 20.toBigDecimal(),
                quoteValue = 820.toBigDecimal()
            ),
            AmortizationRowDTO(
                id = 2,
                periods = 6,
                creditRate = 25.5,
                kindRate = KindOfTaxEnum.ANUAL_EFFECTIVE,
                creditValue = 10_000.toBigDecimal(),
                amortizatedValue = 9_200.toBigDecimal(),
                capitalValue = 802.toBigDecimal(),
                interestValue = 18.toBigDecimal(),
                quoteValue = 820.toBigDecimal()
            )
        )
    }
}
