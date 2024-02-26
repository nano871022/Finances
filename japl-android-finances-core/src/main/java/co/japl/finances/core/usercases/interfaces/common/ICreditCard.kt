package co.japl.finances.core.usercases.interfaces.common

import co.com.japl.finances.iports.dtos.CreditCardDTO

interface ICreditCard {

    fun getAll(): List<CreditCardDTO>

    fun get(codCreditCard:Int):CreditCardDTO?

    fun delete(codeCreditCard:Int):Boolean

    fun create(dto:CreditCardDTO):Int

    fun update(dto:CreditCardDTO):Boolean

}