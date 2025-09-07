package co.com.japl.module.credit.views.creditamortization

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional

class FakeAdditional : IAdditional {
    override fun getAdditional(codeCredit: Int): List<AdditionalCreditDTO> {
        return emptyList()
    }
}
