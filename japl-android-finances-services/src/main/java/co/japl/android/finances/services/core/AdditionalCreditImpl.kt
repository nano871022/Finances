package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.AdditionalCreditMapper
import co.japl.android.finances.services.dao.interfaces.IAdditionalCreditDAO
import co.com.japl.finances.iports.outbounds.IAdditionalRecapPort
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.outbounds.IAdditionalPort
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

class AdditionalCreditImpl @Inject constructor(private val additionalCreditImpl: IAdditionalCreditDAO): IAdditionalRecapPort, IAdditionalPort{
    override fun getListByIdCredit(idCredit: Long): List<AdditionalCreditDTO> {
        return additionalCreditImpl.get(idCredit).map (AdditionalCreditMapper::mapper)
    }

    override fun getAdditional(code: Int): List<AdditionalCreditDTO> {
        return getListByIdCredit(code.toLong())
    }

    override fun delete(code: Int): Boolean {
        return additionalCreditImpl.delete(code)
    }

    override fun create(dto: AdditionalCreditDTO): Boolean {
        return additionalCreditImpl.save(AdditionalCreditMapper.mapper(dto)) > 0
    }

    override fun update(dto: AdditionalCreditDTO): Boolean {
        return additionalCreditImpl.update(AdditionalCreditMapper.mapper(dto)) > 0
    }

    override fun get(id: Int): AdditionalCreditDTO? {
        return additionalCreditImpl.get(id).map { AdditionalCreditMapper.mapper(it) }.getOrNull()
    }


}