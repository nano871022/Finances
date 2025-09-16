package co.com.japl.module.creditcard.views.fakeSvc

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort

class FakeBuyCreditCardSettingPort : IBuyCreditCardSettingPort {
    override fun get(codeBought: Int): BuyCreditCardSettingDTO? {
        TODO("Not yet implemented")
    }

    override fun create(dto: BuyCreditCardSettingDTO): Int {
        TODO("Not yet implemented")
    }

    override fun createOrUpdate(dto: BuyCreditCardSettingDTO): Int {
        TODO("Not yet implemented")
    }

    override fun update(dto: BuyCreditCardSettingDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(codeSetting: Int): Boolean {
        TODO("Not yet implemented")
    }

}
