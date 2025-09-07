package co.com.japl.module.credit.views.forms

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort

class FakeAdditionalFormSvc : IAdditionalFormPort {
    override fun get(id: Int): AdditionalCreditDTO? {
        return null
    }

    override fun create(dto: AdditionalCreditDTO): Boolean {
        return true
    }

    override fun update(dto: AdditionalCreditDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun getAll(codeCredit: Long): List<AdditionalCreditDTO> {
        return emptyList()
    }
}
