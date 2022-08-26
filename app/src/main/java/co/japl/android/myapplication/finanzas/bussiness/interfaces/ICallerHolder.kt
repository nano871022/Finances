package co.japl.android.myapplication.bussiness.interfaces

interface ICallerHolder<T> {
    fun execute(fn: ((T)->Unit)?=null)
}