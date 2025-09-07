package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort

class FakeBuyCreditCardSettingPort : IBuyCreditCardSettingPort {
    override fun get(codeBought: Int): BuyCreditCardSettingDTO? {
        return null
    }

    override fun create(dto: BuyCreditCardSettingDTO): Boolean {
        return true
    }

    override fun update(dto: BuyCreditCardSettingDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun createOrUpdate(dto: BuyCreditCardSettingDTO): Boolean {
        return true
    }
}
