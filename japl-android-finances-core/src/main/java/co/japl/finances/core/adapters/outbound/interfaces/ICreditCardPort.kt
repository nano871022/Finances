package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.CreditCardDTO

interface ICreditCardPort {

    fun getAll():List<CreditCardDTO>

    fun get(id:Int):CreditCardDTO?
}