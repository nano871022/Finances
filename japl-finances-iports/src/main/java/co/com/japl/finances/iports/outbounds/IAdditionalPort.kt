package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO

interface IAdditionalPort {

    fun getAdditional(code:Int):List<AdditionalCreditDTO>

    fun delete(code:Int):Boolean

    fun create(dto: AdditionalCreditDTO):Boolean

    fun update(dto: AdditionalCreditDTO):Boolean

    fun get(id: Int):AdditionalCreditDTO?
}