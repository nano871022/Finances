package co.japl.finances.core.usercases.implement.credit

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO

import co.com.japl.finances.iports.outbounds.IAdditionalPort
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.japl.finances.core.usercases.interfaces.credit.IAdditional
import javax.inject.Inject

class AdditionalImpl @Inject constructor(private val additionalPort: IAdditionalPort,private val creditSvc: ICreditPort) : IAdditional {

    override fun getAdditional(code: Int): List<AdditionalCreditDTO> {
        return additionalPort.getAdditional(code)
    }

    override fun delete(code: Int): Boolean {
        return additionalPort.delete(code)
    }

    override fun create(dto: AdditionalCreditDTO): Boolean {
        creditSvc.getById(dto.creditCode.toInt())?.let {
            it.date.plusMonths(it.periods.toLong()).let {
                dto.endDate = it
            }
        }
        return additionalPort.create(dto)
    }

    override fun update(dto: AdditionalCreditDTO): Boolean {
        return additionalPort.update(dto)
    }

    override fun get(idAdditional: Int): AdditionalCreditDTO? {
        return additionalPort.get(idAdditional)
    }
}