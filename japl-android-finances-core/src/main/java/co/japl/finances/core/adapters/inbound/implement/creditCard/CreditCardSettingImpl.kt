package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.japl.finances.core.usercases.interfaces.creditcard.ICreditCardSetting
import javax.inject.Inject

class CreditCardSettingImpl @Inject constructor(private val creditCardSettingSvc:ICreditCardSetting) : ICreditCardSettingPort {
    override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
        return creditCardSettingSvc.getAll(codeCreditCard)
    }

    override fun delete(codeCreditCard: Int, codeCreditCardSetting: Int): Boolean {
        return creditCardSettingSvc.delete(codeCreditCard,codeCreditCardSetting)
    }

    override fun update(dto: CreditCardSettingDTO): Boolean {
        return creditCardSettingSvc.update(dto)
    }

    override fun create(dto: CreditCardSettingDTO): Int {
        return creditCardSettingSvc.create(dto)
    }
}