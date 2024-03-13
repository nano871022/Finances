package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort
import co.japl.finances.core.usercases.interfaces.creditcard.IBuyCreditCardSetting
import javax.inject.Inject

class BuyCreditCardSettingImpl @Inject constructor(private val buyCCSSvc: IBuyCreditCardSettingPort) : IBuyCreditCardSetting {
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