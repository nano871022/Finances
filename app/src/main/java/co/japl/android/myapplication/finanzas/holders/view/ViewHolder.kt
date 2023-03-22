package co.japl.android.myapplication.holders.view

import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
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
    lateinit var btnAmortization:ImageButton
    lateinit var tvCapitalValue:TextView
    lateinit var tvInterestValue:TextView
    lateinit var llCapitalValue:LinearLayout
    lateinit var llInterestValue:LinearLayout

    fun loadFields(view:View){
        tvCreditValueListSaveItem = view.findViewById(R.id.tvCreditValueListSaveItem)
        tvInterestListSaveItem = view.findViewById(R.id.tvInterestListSaveItem)
        tvNameListSaveItem = view.findViewById(R.id.tvNameListSaveItem)
        tvPeriodListSaveItem = view.findViewById(R.id.tvPeriodListSaveItem)
        tvQuoteCreditListSaveItem = view.findViewById(R.id.tvQuoteCreditListSaveItem)
        tvTypeListSaveItem = view.findViewById(R.id.tvTypeListSaveItem)
        btnDelete = view.findViewById(R.id.btnDelete)
        tvCapitalValue = view.findViewById(R.id.tvCapitalValueList)
        tvInterestValue = view.findViewById(R.id.tvInterestValueList)
        llCapitalValue = view.findViewById(R.id.llCapitalBought)
        llInterestValue = view.findViewById(R.id.llInterestBought)
        btnAmortization = view.findViewById(R.id.btnAmortization)
        llCapitalValue.visibility = View.INVISIBLE
        llInterestValue.visibility = View.INVISIBLE
    }
}