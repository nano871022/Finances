package co.com.japl.module.creditcard.views.fakeSvc

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort


class FakeAmortizationTable : IAmortizationTablePort{
    override fun getAmortization(
        code: Int,
        kind: KindAmortization,
        cache: Boolean
    ): List<AmortizationRowDTO> {
        TODO("Not yet implemented")
    }
}