package co.japl.android.finances.services.interfaces

interface ISaveSvc<T> {
    fun get(values:T):List<T>
}