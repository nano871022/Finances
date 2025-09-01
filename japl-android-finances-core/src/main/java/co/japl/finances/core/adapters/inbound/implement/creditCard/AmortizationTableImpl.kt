package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort
import co.japl.finances.core.usercases.interfaces.creditcard.IAmortizationTable
import javax.inject.Inject

class AmortizationTableImpl @Inject constructor(private val svc: IAmortizationTable): IAmortizationTablePort {

    override fun getAmortization(
        code: Int,
        kind: KindAmortization,
        cache: Boolean
    ): List<AmortizationRowDTO> {
        require(code>0){"Code should not be zero"}
        return svc.getAmortization(code,kind,cache)
    }
}