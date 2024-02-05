package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum

interface ITaxPort {

    fun get(codCreditCard:Int,month:Int, year:Int,kind: KindInterestRateEnum):TaxDTO?

    fun getByCreditCard(codCreditCard:Int):List<TaxDTO>?

    fun delete(code:Int):Boolean

    fun enable(code:Int):Boolean

    fun disable(code:Int):Boolean

    fun create(dto:TaxDTO):Boolean

    fun update(dto:TaxDTO):Boolean

    fun getById(codeCreditRate:Int):TaxDTO?

}