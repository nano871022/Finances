package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.BuyCreditCardSettingDTO

object BuyCreditCardSettingMapper {

    fun mapper(buyCreditCardSetting:BuyCreditCardSettingDTO):co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO {
        return co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO(
            buyCreditCardSetting.id,
            buyCreditCardSetting.codeBuyCreditCard,
            buyCreditCardSetting.codeCreditCardSetting,
            buyCreditCardSetting.create,
            buyCreditCardSetting.active
        )
    }
    fun mapper(buyCreditCardSetting:co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO):BuyCreditCardSettingDTO {
        return BuyCreditCardSettingDTO(
            buyCreditCardSetting.id,
            buyCreditCardSetting.codeBuyCreditCard,
            buyCreditCardSetting.codeCreditCardSetting,
            buyCreditCardSetting.create,
            buyCreditCardSetting.active
        )
    }

}