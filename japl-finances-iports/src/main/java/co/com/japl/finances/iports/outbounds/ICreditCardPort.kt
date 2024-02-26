package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CreditCardDTO

interface ICreditCardPort {

    fun getAll():List<CreditCardDTO>

    fun get(id:Int):CreditCardDTO?

    fun delete(codeCreditCard:Int):Boolean

    fun create(dto:CreditCardDTO):Int

    fun update(dto:CreditCardDTO):Boolean
}