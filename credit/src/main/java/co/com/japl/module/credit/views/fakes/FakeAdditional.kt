package co.com.japl.module.credit.views.fakes

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional

class FakeAdditional : IAdditional {
    override fun getAdditional(code: Int): List<AdditionalCreditDTO> {
        TODO("Not yet implemented")
    }

    override fun delete(code: Int): Boolean {
        TODO("Not yet implemented")
    }

}
