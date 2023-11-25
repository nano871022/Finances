package co.japl.finances.core.adapters.inbound.implement.recap

import co.japl.finances.core.adapters.inbound.interfaces.recap.ICreditCardPort
import co.japl.finances.core.dto.CreditCardDTO
import co.japl.finances.core.usercases.interfaces.recap.ICreditCard
import javax.inject.Inject

class CreditCardImpl @Inject constructor(private val creditCardSvc: ICreditCard) :ICreditCardPort {
    override fun getAll(): List<CreditCardDTO> {
        return creditCardSvc.getAll()
    }
}