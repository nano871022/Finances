package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.AdditionalCreditMapper
import co.japl.android.finances.services.interfaces.IAdditionalCreditSvc
import co.japl.finances.core.adapters.outbound.interfaces.IAdditionalRecapPort
import co.japl.finances.core.dto.AdditionalCreditDTO
import javax.inject.Inject

class AdditionalCreditImpl @Inject constructor(private val additionalCreditImpl:IAdditionalCreditSvc): IAdditionalRecapPort{
    override fun getListByIdCredit(idCredit: Long): List<AdditionalCreditDTO> {
        return additionalCreditImpl.get(idCredit).map (AdditionalCreditMapper::mapper)
    }


}