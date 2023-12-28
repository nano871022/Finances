package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.CreditCardSettingDTO

interface ICreditCardSettingPort {

    fun getAll(codeCreditCard:Int):List<CreditCardSettingDTO>

    fun delete(codeCreditCard:Int,codeCreditCardSetting:Int):Boolean

    fun update(dto: CreditCardSettingDTO):Boolean

    fun create(dto:CreditCardSettingDTO):Int

}