package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort

class FakeCreditCardPort : ICreditCardPort {
    override fun getCreditCard(codeCreditCard: Int): CreditCardDTO? {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<CreditCardDTO> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun create(dto: CreditCardDTO): Int {
        TODO("Not yet implemented")
    }

    override fun update(dto: CreditCardDTO): Boolean {
        TODO("Not yet implemented")
    }

}
