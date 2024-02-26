package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO

interface IBuyCreditCardSettingPort {

    fun get(codeBought:Int):BuyCreditCardSettingDTO?

    fun create(dto:BuyCreditCardSettingDTO):Int

    fun update(dto:BuyCreditCardSettingDTO):Boolean

}