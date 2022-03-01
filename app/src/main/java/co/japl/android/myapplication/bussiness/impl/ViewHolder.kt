package co.japl.android.myapplication.bussiness.impl

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    lateinit var tvQuoteCreditListSaveItem:TextView
    lateinit var tvPeriodListSaveItem:TextView
    lateinit var tvInterestListSaveItem:TextView
    lateinit var tvCreditValueListSaveItem:TextView
    lateinit var tvNameListSaveItem:TextView
    lateinit var tvTypeListSaveItem:TextView
    lateinit var btnDelete:ImageButton

    fun loadFields(view:View){
        tvCreditValueListSaveItem = view.findViewById(R.id.tvCreditValueListSaveItem)
        tvInterestListSaveItem = view.findViewById(R.id.tvInterestListSaveItem)
        tvNameListSaveItem = view.findViewById(R.id.tvNameListSaveItem)
        tvPeriodListSaveItem = view.findViewById(R.id.tvPeriodListSaveItem)
        tvQuoteCreditListSaveItem = view.findViewById(R.id.tvQuoteCreditListSaveItem)
        tvTypeListSaveItem = view.findViewById(R.id.tvTypeListSaveItem)
        btnDelete = view.findViewById(R.id.btnDelete)
    }
}