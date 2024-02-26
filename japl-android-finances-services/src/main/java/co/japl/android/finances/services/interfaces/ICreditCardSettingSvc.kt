package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.CreditCardSettingDTO

interface ICreditCardSettingSvc: SaveSvc<CreditCardSettingDTO> {
    fun getAll(codeCC:Int): List<CreditCardSettingDTO>
}