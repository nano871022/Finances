package co.japl.android.myapplication.finanzas.holders.interfaces

interface ICallerHolder<T> {
    fun execute(fn: ((T)->Unit)?=null)
}