package co.com.japl.module.creditcard.views.fakeSvc

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import java.time.LocalDate

class CreditRateFake : ITaxPort {
    override fun get(
        codCreditCard: Int,
        month: Int,
        year: Int,
        kind: KindInterestRateEnum
    ): TaxDTO? {
        TODO("Not yet implemented")
    }

    override fun getById(codeCreditRate: Int): TaxDTO? {
        TODO("Not yet implemented")
    }

    override fun getByCreditCard(codCreditCard: Int): List<TaxDTO>? {
        TODO("Not yet implemented")
    }

    override fun getByCreditCard(
        codeCreditCard: Int,
        cutOff: LocalDate
    ): List<TaxDTO> {
        TODO("Not yet implemented")
    }

    override fun delete(code: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun enable(code: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun disable(code: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun create(dto: TaxDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(dto: TaxDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun clone(code: Int): Boolean {
        TODO("Not yet implemented")
    }
}