package co.japl.android.myapplication.finanzas.holders.interfaces

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface IItemHolder<T,S> {
    fun loadFields()
    fun setFields(values: T, callback: (S)->Unit)
}