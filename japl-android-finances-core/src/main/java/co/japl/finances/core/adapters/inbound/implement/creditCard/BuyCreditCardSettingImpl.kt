package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.japl.finances.core.usercases.interfaces.creditcard.IBuyCreditCardSetting
import javax.inject.Inject

class BuyCreditCardSettingImpl @Inject constructor(private val buyCCSSvc: IBuyCreditCardSetting): IBuyCreditCardSettingPort {
    override fun get(codeBought: Int): BuyCreditCardSettingDTO? {
        return buyCCSSvc.get(codeBought)
    }

    override fun create(dto: BuyCreditCardSettingDTO): Int {
        return buyCCSSvc.create(dto)
    }

    override fun update(dto: BuyCreditCardSettingDTO): Boolean {
        return buyCCSSvc.update(dto)
    }
}