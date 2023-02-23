package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO

interface ICreditCardSettingSvc {
    fun getAll(codeCC:Int): List<CreditCardSettingDTO>
}