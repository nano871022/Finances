package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import co.japl.finances.core.usercases.interfaces.credit.IAmortizationTable
import javax.inject.Inject

class AmortizationTableImpl @Inject constructor(private val svc: IAmortizationTable):
    IAmortizationTablePort {

    override fun getAmortization(
        code: Int,
        kind: KindAmortization,
        cache: Boolean
    ): List<AmortizationRowDTO> {
        require(code>0){"Code should not be zero"}
        return svc.getAmortization(code,kind,cache)
    }
}