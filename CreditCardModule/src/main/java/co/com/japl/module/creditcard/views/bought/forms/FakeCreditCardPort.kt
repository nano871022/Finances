package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort

class FakeCreditCardPort : ICreditCardPort {
    override fun getCreditCards(): List<CreditCardDTO> {
        return emptyList()
    }

    override fun getCreditCard(code: Int): CreditCardDTO? {
        return null
    }

    override fun create(creditCard: CreditCardDTO): Boolean {
        return true
    }

    override fun update(creditCard: CreditCardDTO): Boolean {
        return true
    }

    override fun delete(code: Int): Boolean {
        return true
    }
}
