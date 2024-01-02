package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.CreditCardDTO

interface ICreditCardPort {

    fun getCreditCard(codeCreditCard:Int): CreditCardDTO?

    fun getAll():List<CreditCardDTO>

    fun delete(id:Int):Boolean

    fun create(dto:CreditCardDTO):Int

    fun update(dto:CreditCardDTO):Boolean

}