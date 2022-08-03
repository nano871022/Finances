package co.japl.android.myapplication.bussiness.interfaces

interface ISpinnerHolder<T> {
    fun lists(fn: ((T)->Unit)?=null)
}