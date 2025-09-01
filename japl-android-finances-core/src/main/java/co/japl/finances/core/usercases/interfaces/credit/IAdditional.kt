package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO

interface IAdditional {
    fun getAdditional(code: Int): List<AdditionalCreditDTO>

    fun delete(code: Int): Boolean

    fun create(dto: AdditionalCreditDTO):Boolean

    fun update(dto: AdditionalCreditDTO):Boolean

    fun get(idAdditional: Int):AdditionalCreditDTO?
}