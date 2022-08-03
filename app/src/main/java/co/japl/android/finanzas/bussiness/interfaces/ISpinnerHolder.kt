package co.japl.android.finanzas.bussiness.interfaces

import android.widget.AdapterView

interface ISpinnerHolder<T> {
    fun lists(fn: ((T)->Unit)?=null)
}