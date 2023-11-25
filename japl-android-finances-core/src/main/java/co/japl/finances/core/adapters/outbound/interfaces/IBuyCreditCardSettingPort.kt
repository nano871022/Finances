package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.BuyCreditCardSettingDTO

interface IBuyCreditCardSettingPort {

    fun getAll():List<BuyCreditCardSettingDTO>

    fun get(id:Int):BuyCreditCardSettingDTO?

}