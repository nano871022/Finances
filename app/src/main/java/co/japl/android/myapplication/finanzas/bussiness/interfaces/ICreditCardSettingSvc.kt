package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc

interface ICreditCardSettingSvc: SaveSvc<CreditCardSettingDTO> {
    fun getAll(codeCC:Int): List<CreditCardSettingDTO>
}