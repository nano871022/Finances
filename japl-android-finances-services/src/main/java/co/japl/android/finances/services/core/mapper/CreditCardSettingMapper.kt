package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditCardSettingDTO

object CreditCardSettingMapper {

    fun mapper(creditCardSetting:CreditCardSettingDTO):co.com.japl.finances.iports.dtos.CreditCardSettingDTO {
        return co.com.japl.finances.iports.dtos.CreditCardSettingDTO(
            creditCardSetting.id,
            creditCardSetting.codeCreditCard,
            creditCardSetting.name,
            creditCardSetting.value,
            creditCardSetting.type,
            creditCardSetting.create,
            creditCardSetting.active
        )
    }

    fun mapper(creditCardSetting:co.com.japl.finances.iports.dtos.CreditCardSettingDTO):CreditCardSettingDTO {
        return CreditCardSettingDTO(
            creditCardSetting.id,
            creditCardSetting.codeCreditCard,
            creditCardSetting.name,
            creditCardSetting.value,
            creditCardSetting.type,
            creditCardSetting.create,
            creditCardSetting.active
        )
    }

}