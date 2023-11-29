package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.BuyCreditCardSettingMapper
import co.japl.android.finances.services.interfaces.IBuyCreditCardSettingSvc
import co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import javax.inject.Inject

class BuyCreditCardSettingImpl @Inject constructor(private val buyCCSettingSvc:IBuyCreditCardSettingSvc) : IBuyCreditCardSettingPort {
    override fun getAll(): List<BuyCreditCardSettingDTO> {
        return buyCCSettingSvc.getAll().map(BuyCreditCardSettingMapper::mapper)
    }

    override fun get(id: Int): BuyCreditCardSettingDTO? {
        val buyCCSetting =  buyCCSettingSvc.get(id)
        if(buyCCSetting.isPresent) {
            return BuyCreditCardSettingMapper.mapper(buyCCSetting.get())
        }
        return null
    }
}