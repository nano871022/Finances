package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.CreditCardBoughtDTO
import co.japl.finances.core.dto.CreditCardSettingDTO

interface ICreditCardSettingPort {

    fun get(id:Int):CreditCardSettingDTO?

}