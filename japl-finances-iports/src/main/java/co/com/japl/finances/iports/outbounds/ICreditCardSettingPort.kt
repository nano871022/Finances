package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO

interface ICreditCardSettingPort {

    fun get(id:Int):CreditCardSettingDTO?

    fun getAll(codeCreditCard:Int):List<CreditCardSettingDTO>

    fun delete(codeCreditCard: Int,codeCreditCardSetting: Int):Boolean

    fun update(dto:CreditCardSettingDTO):Boolean

    fun create(dto:CreditCardSettingDTO):Int

}