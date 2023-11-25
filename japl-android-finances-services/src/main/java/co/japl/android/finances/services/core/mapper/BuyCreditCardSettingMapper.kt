package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.BuyCreditCardSettingDTO

object BuyCreditCardSettingMapper {

    fun mapper(buyCreditCardSetting:BuyCreditCardSettingDTO):co.japl.finances.core.dto.BuyCreditCardSettingDTO {
        return co.japl.finances.core.dto.BuyCreditCardSettingDTO(
            buyCreditCardSetting.id,
            buyCreditCardSetting.codeBuyCreditCard,
            buyCreditCardSetting.codeCreditCardSetting,
            buyCreditCardSetting.create,
            buyCreditCardSetting.active
        )
    }

}