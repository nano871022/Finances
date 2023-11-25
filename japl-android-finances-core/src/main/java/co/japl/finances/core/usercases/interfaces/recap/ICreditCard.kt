package co.japl.finances.core.usercases.interfaces.recap

import co.japl.finances.core.dto.CreditCardDTO

interface ICreditCard {

    fun getAll(): List<CreditCardDTO>

}