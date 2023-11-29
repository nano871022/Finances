package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditCardSettingMapper
import co.japl.android.finances.services.interfaces.ICreditCardSettingSvc
import co.com.japl.finances.iports.outbounds.ICreditCardSettingPort
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import javax.inject.Inject

class CreditCardSettingImpl  @Inject constructor(private val creditCardSettingSvc:ICreditCardSettingSvc):ICreditCardSettingPort {

    override fun get(id: Int): CreditCardSettingDTO? {
        val creditCardSetting = creditCardSettingSvc.get(id)
        if(creditCardSetting.isPresent) {
            return CreditCardSettingMapper.mapper(creditCardSetting.get())
        }
        return null
    }
}