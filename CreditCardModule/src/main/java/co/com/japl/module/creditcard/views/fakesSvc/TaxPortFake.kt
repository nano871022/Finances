package co.com.japl.module.creditcard.views.fakesSvc

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort

class TaxPortFake : ITaxPort {
    override fun get(codCreditCard: Int, month: Int, year: Int, kind: KindInterestRateEnum): TaxDTO? {
        return null
    }

    override fun getByCreditCard(codCreditCard: Int): List<TaxDTO>? {
        return null
    }

    override fun getById(code: Int): TaxDTO? {
        return null
    }

    override fun create(dto: TaxDTO): Boolean {
        return true
    }

    override fun update(dto: TaxDTO): Boolean {
        return true
    }

    override fun delete(code: Int): Boolean {
        return true
    }

    override fun enable(code: Int): Boolean {
        return true
    }

    override fun disable(code: Int): Boolean {
        return true
    }

    override fun clone(code: Int): Boolean {
        return true
    }
}
