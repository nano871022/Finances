package co.japl.android.myapplication.finanzas.bussiness.interfaces

interface ISaveSvc<T> {
    fun get(values:T):List<T>
}