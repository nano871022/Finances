package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO

interface IBuyCreditCardSetting {

    fun get(codeBought:Int):BuyCreditCardSettingDTO?

    fun create(dto:BuyCreditCardSettingDTO):Int

    fun update(dto:BuyCreditCardSettingDTO):Boolean

    fun delete(codeSetting:Int):Boolean

}