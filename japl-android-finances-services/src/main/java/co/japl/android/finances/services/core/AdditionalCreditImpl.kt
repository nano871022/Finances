package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.AdditionalCreditMapper
import co.japl.android.finances.services.dao.interfaces.IAdditionalCreditDAO
import co.com.japl.finances.iports.outbounds.IAdditionalRecapPort
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import javax.inject.Inject

class AdditionalCreditImpl @Inject constructor(private val additionalCreditImpl: IAdditionalCreditDAO): IAdditionalRecapPort{
    override fun getListByIdCredit(idCredit: Long): List<AdditionalCreditDTO> {
        return additionalCreditImpl.get(idCredit).map (AdditionalCreditMapper::mapper)
    }


}