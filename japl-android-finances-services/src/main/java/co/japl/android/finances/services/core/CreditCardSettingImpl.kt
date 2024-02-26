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

    override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
        return creditCardSettingSvc.getAll(codeCreditCard).map { CreditCardSettingMapper.mapper(it) }
    }

    override fun delete(codeCreditCard: Int, codeCreditCardSetting: Int): Boolean {
        require(codeCreditCard > 0)
        require(codeCreditCardSetting > 0)
        if(get(codeCreditCard) == null){
            return false
        }

        return creditCardSettingSvc.delete(codeCreditCardSetting)
    }

    override fun update(dto: CreditCardSettingDTO): Boolean {
        require(dto.id > 0)
        if(get(dto.id) == null){
            return false
        }
        return creditCardSettingSvc.save(CreditCardSettingMapper.mapper(dto)) > 0
    }

    override fun create(dto: CreditCardSettingDTO): Int {
        require(dto.id == 0)
        if(get(dto.id) != null){
            return -1
        }
        return creditCardSettingSvc.save(CreditCardSettingMapper.mapper(dto)).toInt()
    }
}