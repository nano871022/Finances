package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.dtos.CreditCardDTO

interface ICreditCardPort {

    fun getCreditCard(codeCreditCard:Int): CreditCardDTO?

    fun getAll():List<CreditCardDTO>

    fun delete(id:Int):Boolean

}