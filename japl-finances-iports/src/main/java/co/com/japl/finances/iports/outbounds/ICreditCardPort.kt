package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CreditCardDTO

interface ICreditCardPort {

    fun getAll():List<CreditCardDTO>

    fun get(id:Int):CreditCardDTO?
}