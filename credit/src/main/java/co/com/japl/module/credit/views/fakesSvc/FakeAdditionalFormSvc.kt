package co.com.japl.module.credit.views.forms

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort

class FakeAdditionalFormSvc : IAdditionalFormPort {
    override fun create(dto: AdditionalCreditDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(dto: AdditionalCreditDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(idAdditional: Int): AdditionalCreditDTO? {
        TODO("Not yet implemented")
    }

}
