package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDate

interface ITaxPort {

    fun get(codCreditCard:Int,month:Int, year:Int,kind: KindInterestRateEnum): TaxDTO?

    fun getById(codeCreditRate:Int):TaxDTO?

    fun getByCreditCard(codCreditCard:Int):List<TaxDTO>?

    fun getByCreditCard(codeCreditCard:Int,cutOff:LocalDate):List<TaxDTO>

    fun delete(code:Int):Boolean

    fun enable(code:Int):Boolean

    fun disable(code:Int):Boolean

    fun create(dto:TaxDTO):Boolean

    fun update(dto: TaxDTO):Boolean

    fun clone(code:Int):Boolean
}