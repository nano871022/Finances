package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO

interface IBuyCreditCardSettingPort {

    fun getAll():List<BuyCreditCardSettingDTO>

    fun get(id:Int):BuyCreditCardSettingDTO?

}