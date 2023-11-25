package co.japl.finances.core.adapters.inbound.interfaces.recap

import co.japl.finances.core.dto.CreditCardDTO

interface ICreditCardPort {

    fun getAll():List<CreditCardDTO>
}