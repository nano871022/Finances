package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO

interface IAdditionalRecapPort {

    fun getListByIdCredit(idCredit:Long): List<AdditionalCreditDTO>
}