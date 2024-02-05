package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.japl.finances.core.usercases.interfaces.creditcard.ITax
import javax.inject.Inject

class TaxImpl @Inject constructor(private val service:ITaxPort): ITax {
    override fun get(
        codCreditCard: Int,
        month: Int,
        year: Int,
        kind: KindInterestRateEnum
    ): TaxDTO? {
        return service.get(codCreditCard,month,year,kind)
    }

    override fun getById(codeCreditRate: Int): TaxDTO? {
        return service.getById(codeCreditRate)
    }

    override fun create(dto: TaxDTO): Boolean {
        return service.create(dto)
    }

    override fun update(dto: TaxDTO): Boolean {
        return service.update(dto)
    }

    override fun getByCreditCard(codCreditCard: Int): List<TaxDTO>? {
        return service.getByCreditCard(codCreditCard)
    }

    override fun delete(code: Int): Boolean {
        return service.delete(code)
    }

    override fun enable(code: Int): Boolean {
        return service.enable(code)
    }

    override fun disable(code: Int): Boolean {
        return service.disable(code)
    }
}