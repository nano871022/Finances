package co.japl.android.myapplication.finanzas.holders.interfaces

import android.view.View

interface IHolder<T> {
    fun setFields(actions: View.OnClickListener?)
    fun loadFields(values: T)
    fun downLoadFields():T
    fun cleanField()
    fun validate():Boolean
}