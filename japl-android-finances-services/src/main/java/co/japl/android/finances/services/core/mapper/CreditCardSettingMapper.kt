package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditCardSettingDTO

object CreditCardSettingMapper {

    fun mapper(creditCardSetting:CreditCardSettingDTO):co.japl.finances.core.dto.CreditCardSettingDTO {
        return co.japl.finances.core.dto.CreditCardSettingDTO(
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