package co.japl.android.myapplication.finanzas.holders.interfaces

interface ISpinnerHolder<T> {
    fun lists(fn: ((T)->Unit)?=null)
}