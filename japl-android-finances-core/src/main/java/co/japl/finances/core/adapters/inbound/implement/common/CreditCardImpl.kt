package co.japl.finances.core.adapters.inbound.implement.common

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.common.ICreditCardPort
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import javax.inject.Inject

class CreditCardImpl @Inject constructor(private val creditCardSvc:ICreditCard):ICreditCardPort {
    override fun getCreditCard(codeCreditCard: Int): CreditCardDTO? {
        return creditCardSvc.get(codeCreditCard)
    }
}