package co.japl.android.myapplication.bussiness.interfaces

import android.widget.AdapterView

interface ISpinnerHolder<T> {
    fun lists(fn: ((T)->Unit)?=null)
}