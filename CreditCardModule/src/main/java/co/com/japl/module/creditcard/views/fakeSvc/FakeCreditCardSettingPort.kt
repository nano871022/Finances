package co.com.japl.module.creditcard.views.fakeSvc

import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort

class FakeCreditCardSettingPort : ICreditCardSettingPort {
    override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
        TODO("Not yet implemented")
    }

    override fun get(
        codeCreditCard: Int,
        codeCreditCardSetting: Int
    ): CreditCardSettingDTO? {
        TODO("Not yet implemented")
    }

    override fun delete(
        codeCreditCard: Int,
        codeCreditCardSetting: Int
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(dto: CreditCardSettingDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun create(dto: CreditCardSettingDTO): Int {
        TODO("Not yet implemented")
    }

}
