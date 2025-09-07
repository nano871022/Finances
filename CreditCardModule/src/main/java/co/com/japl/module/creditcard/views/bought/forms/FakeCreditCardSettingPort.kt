package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort

class FakeCreditCardSettingPort : ICreditCardSettingPort {
    override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
        return emptyList()
    }

    override fun get(code: Int): CreditCardSettingDTO? {
        return null
    }

    override fun delete(code: Int): Boolean {
        return true
    }

    override fun save(dto: CreditCardSettingDTO): Boolean {
        return true
    }
}
