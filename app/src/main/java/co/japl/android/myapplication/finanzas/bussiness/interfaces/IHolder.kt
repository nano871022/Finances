package co.japl.android.myapplication.bussiness.interfaces

import android.view.View
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO

interface IHolder<T> {
    fun setFields(actions: View.OnClickListener?)
    fun loadFields(values: T)
    fun downLoadFields():T
    fun cleanField()
    fun validate():Boolean
}