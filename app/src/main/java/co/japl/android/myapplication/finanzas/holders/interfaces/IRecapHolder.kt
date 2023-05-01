package co.japl.android.myapplication.finanzas.holders.interfaces

import android.view.View
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO

interface IRecapHolder<T> {
    fun setFields(actions: View.OnClickListener?)
    fun loadFields(fn: ((T)->Unit)?=null)
}