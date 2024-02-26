package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.outbounds.ICreditCardSettingPort
import co.japl.finances.core.usercases.interfaces.creditcard.ICreditCardSetting
import javax.inject.Inject

class CreditCardSettingImpl @Inject constructor(private val creditCardSettingSvc:ICreditCardSettingPort):ICreditCardSetting {
    override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
        return creditCardSettingSvc.getAll(codeCreditCard)
    }

    override fun get(codeCreditCard: Int, codeCreditCardSetting: Int): CreditCardSettingDTO? {
        require(codeCreditCard > 0,{"Code Credit Card has not valid value"})
        require(codeCreditCardSetting > 0, {"Code Credit Card Setting has not valid value"})
        return creditCardSettingSvc.get(codeCreditCardSetting)
    }

    override fun delete(codeCreditCard: Int, codeCreditCardSetting: Int): Boolean {
        return creditCardSettingSvc.delete(codeCreditCard,codeCreditCardSetting)
    }

    override fun update(creditCardSettingDTO: CreditCardSettingDTO): Boolean {
        return creditCardSettingSvc.update(creditCardSettingDTO)
    }

    override fun create(creditCardSettingDTO: CreditCardSettingDTO): Int {
        return creditCardSettingSvc.create(creditCardSettingDTO)
    }
}