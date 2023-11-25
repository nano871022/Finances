package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.AdditionalCreditDTO

interface IAdditionalRecapPort {

    fun getListByIdCredit(idCredit:Long): List<AdditionalCreditDTO>
}