package co.japl.android.myapplication.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R

class BoughtViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    lateinit var tvBoughtName:TextView
    lateinit var tvCreditValueBought:TextView
    lateinit var tvQuotesBought:TextView
    lateinit var tvMonthBought:TextView
    lateinit var tvCapitalBought:TextView
    lateinit var tvInterestBought:TextView
    lateinit var btnBoughtDelete:ImageButton
    lateinit var tvTotalQuoteBought:TextView
    lateinit var tvBoughtDate:TextView
    lateinit var tvPendingToPay:TextView

    fun loadFields(view:View){
        tvBoughtName = view.findViewById(R.id.tvBoughtName)
        tvCreditValueBought = view.findViewById(R.id.tvCreditValueBought)
        tvQuotesBought = view.findViewById(R.id.tvQuotesBought)
        tvMonthBought = view.findViewById(R.id.tvMonthBought)
        tvCapitalBought = view.findViewById(R.id.tvCapitalBought)
        tvInterestBought = view.findViewById(R.id.tvInterestBought)
        btnBoughtDelete = view.findViewById(R.id.btnBoughtDelete)
        tvTotalQuoteBought = view.findViewById(R.id.tvTotalQuoteBought)
        tvBoughtDate = view.findViewById(R.id.tvBoughtDate)
        tvPendingToPay = view.findViewById(R.id.tvPendingToPay)
    }
}