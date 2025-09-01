package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.AdditionalCreditDTO

interface IAdditional {

    fun getAdditional(code:Int):List<AdditionalCreditDTO>

    fun delete(code:Int):Boolean

}