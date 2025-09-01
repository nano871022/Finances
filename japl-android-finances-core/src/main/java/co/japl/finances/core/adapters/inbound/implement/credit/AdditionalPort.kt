package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort
import javax.inject.Inject

class AdditionalPort @Inject constructor(private val additionalSvc:co.japl.finances.core.usercases.interfaces.credit.IAdditional) : IAdditional , IAdditionalFormPort{
    override fun getAdditional(code: Int): List<AdditionalCreditDTO> {
        require(code > 0, { "El código debe ser mayor a cero" })
        return additionalSvc.getAdditional(code)
    }

    override fun delete(code: Int): Boolean {
        require(code > 0, { "El código debe ser mayor a cero" })
        return additionalSvc.delete(code)
    }

    override fun create(dto: AdditionalCreditDTO): Boolean {
        require(dto.id == 0, {"El código debe ser cero"})
        require(dto.creditCode.toInt() > 0, { "El código del crédito debe ser cero" })
        return additionalSvc.create(dto)
    }

    override fun update(dto: AdditionalCreditDTO): Boolean {
        require(dto.id > 0, { "El código debe ser mayor a cero" })
        require(dto.creditCode > 0, { "El código del crédito debe ser mayor a cero" })
        return additionalSvc.update(dto)
    }

    override fun get(idAdditional: Int): AdditionalCreditDTO? {
        require(idAdditional > 0, { "El código  debe ser mayor a cero" })
        return additionalSvc.get(idAdditional)
    }

}