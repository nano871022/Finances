package co.japl.android.myapplication.holders.view

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R

class TaxItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var month:TextView
    lateinit var year:TextView
    lateinit var tax:TextView
    lateinit var delete:ImageButton

    fun loadFields(){
        month = view.findViewById(R.id.tvMonthItemTCC)
        year = view.findViewById(R.id.tvYearItemTCC)
        tax = view.findViewById(R.id.tvValueItemTCC)
        delete = view.findViewById(R.id.btnDeleteItemTCC)
    }
}