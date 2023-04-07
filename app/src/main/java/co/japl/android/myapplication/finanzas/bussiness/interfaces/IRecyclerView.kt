package co.japl.android.myapplication.finanzas.bussiness.interfaces

interface IRecyclerView<T> {
    fun loadRecycler(data:MutableList<T>)
}