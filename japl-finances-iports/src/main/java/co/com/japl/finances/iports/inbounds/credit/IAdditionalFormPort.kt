package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO

interface IAdditionalFormPort {

    fun create(dto: AdditionalCreditDTO):Boolean

    fun update(dto: AdditionalCreditDTO):Boolean

    fun get(idAdditional: Int):AdditionalCreditDTO?
}