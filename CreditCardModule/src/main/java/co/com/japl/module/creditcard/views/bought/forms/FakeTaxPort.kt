package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort

class FakeTaxPort : ITaxPort {
    override fun get(
        codeCreditCard: Int,
        month: Int,
        year: Int,
        kind: KindInterestRateEnum
    ): TaxDTO? {
        return null
    }

    override fun get(codeCreditRate: Int): TaxDTO? {
        return null
    }

    override fun get(codeCreditCard: Int): List<TaxDTO> {
        return emptyList()
    }

    override fun create(tax: TaxDTO): Boolean {
        return true
    }

    override fun update(tax: TaxDTO): Boolean {
        return true
    }

    override fun delete(code: Int): Boolean {
        return true
    }
}
