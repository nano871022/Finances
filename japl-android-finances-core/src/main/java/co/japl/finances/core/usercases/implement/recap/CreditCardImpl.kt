package co.japl.finances.core.usercases.implement.recap

import co.japl.finances.core.adapters.outbound.interfaces.ICreditCardPort
import co.japl.finances.core.dto.CreditCardDTO
import co.japl.finances.core.usercases.interfaces.recap.ICreditCard
import javax.inject.Inject

class CreditCardImpl  @Inject constructor(private val creditCardSvc:ICreditCardPort) : ICreditCard {
    override fun getAll(): List<CreditCardDTO> {
        return creditCardSvc.getAll()
    }
}