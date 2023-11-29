package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO

interface ICreditCardSettingPort {

    fun get(id:Int):CreditCardSettingDTO?

}