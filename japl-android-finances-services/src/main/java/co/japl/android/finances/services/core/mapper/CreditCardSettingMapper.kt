package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.CreditCardSettingDTO

object CreditCardSettingMapper {

    fun mapper(creditCardSetting:CreditCardSettingDTO):co.com.japl.finances.iports.dtos.CreditCardSettingDTO {
        return co.com.japl.finances.iports.dtos.CreditCardSettingDTO(
            id=creditCardSetting.id,
            codeCreditCard = creditCardSetting.codeCreditCard,
            name=creditCardSetting.name,
            value=creditCardSetting.value,
            type=creditCardSetting.type,
            create=creditCardSetting.create,
            active=creditCardSetting.active
        )
    }

    fun mapper(creditCardSetting:co.com.japl.finances.iports.dtos.CreditCardSettingDTO):CreditCardSettingDTO {
        return CreditCardSettingDTO(
            id=creditCardSetting.id,
            codeCreditCard = creditCardSetting.codeCreditCard,
            name = creditCardSetting.name,
            value = creditCardSetting.value,
            type = creditCardSetting.type,
            create = creditCardSetting.create,
            active = creditCardSetting.active
        )
    }

}