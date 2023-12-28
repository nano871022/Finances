package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import javax.inject.Inject

class CreditCardImpl  @Inject constructor(private val creditCardSvc:ICreditCardPort) : ICreditCard {
    override fun getAll(): List<CreditCardDTO> {
        return creditCardSvc.getAll()
    }

    override fun get(codCreditCard: Int): CreditCardDTO? {
        return creditCardSvc.get(codCreditCard)
    }

    override fun delete(codeCreditCard: Int): Boolean {
        return creditCardSvc.delete(codeCreditCard)
    }
}