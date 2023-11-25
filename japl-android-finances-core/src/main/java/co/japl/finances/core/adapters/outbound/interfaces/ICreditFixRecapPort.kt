package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.CreditDTO

interface ICreditFixRecapPort{

    fun getAll():List<CreditDTO>

}