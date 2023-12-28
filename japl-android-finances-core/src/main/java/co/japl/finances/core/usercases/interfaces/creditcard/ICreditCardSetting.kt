package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.CreditCardSettingDTO

interface ICreditCardSetting {

    fun getAll(codeCreditCard:Int):List<CreditCardSettingDTO>

    fun delete(codeCreditCard: Int, codeCreditCardSetting:Int):Boolean

    fun update(creditCardSettingDTO: CreditCardSettingDTO):Boolean

    fun create(creditCardSettingDTO: CreditCardSettingDTO):Int
}