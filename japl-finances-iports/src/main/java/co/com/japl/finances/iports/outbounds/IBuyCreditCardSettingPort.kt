package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO

interface IBuyCreditCardSettingPort {

    fun getAll():List<BuyCreditCardSettingDTO>

    fun get(id:Int):BuyCreditCardSettingDTO?
    fun create(dto:BuyCreditCardSettingDTO):Int
    fun update(dto:BuyCreditCardSettingDTO):Boolean

    fun delete(id:Int):Boolean
}