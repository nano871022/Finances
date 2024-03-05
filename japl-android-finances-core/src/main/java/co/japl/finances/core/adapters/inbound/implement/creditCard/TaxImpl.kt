package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.japl.finances.core.usercases.interfaces.creditcard.ITax
import java.time.LocalDate
import javax.inject.Inject

class TaxImpl  @Inject constructor(val taxSvc:ITax): ITaxPort {
    override fun get(
        codCreditCard: Int,
        month: Int,
        year: Int,
        kind: KindInterestRateEnum
    ): TaxDTO? {
        return taxSvc.get(codCreditCard,month,year,kind)
    }

    override fun getById(codeCreditRate: Int): TaxDTO? {
        return taxSvc.getById(codeCreditRate)
    }

    override fun getByCreditCard(codCreditCard: Int): List<TaxDTO>? {
        return taxSvc.getByCreditCard(codCreditCard)
    }

    override fun getByCreditCard(codeCreditCard: Int, cutOff: LocalDate): List<TaxDTO> {
        return taxSvc.getByCreditCard(codeCreditCard,cutOff)?: emptyList()
    }

    override fun delete(code: Int): Boolean {
        return taxSvc.delete(code)
    }

    override fun enable(code: Int): Boolean {
        return taxSvc.enable(code)
    }

    override fun disable(code: Int): Boolean {
        return taxSvc.disable(code)
    }

    override fun create(dto: TaxDTO): Boolean {
        return taxSvc.create(dto)
    }

    override fun update(dto: TaxDTO): Boolean {
        return taxSvc.update(dto)
    }

}