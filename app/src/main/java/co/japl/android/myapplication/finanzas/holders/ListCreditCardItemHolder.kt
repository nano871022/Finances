package co.japl.android.myapplication.holders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R

class ListCreditCardItemHolder(var itemView:View) : RecyclerView.ViewHolder(itemView)  {
    lateinit var name:TextView
    lateinit var cutOffDay:TextView
    lateinit var warningQuote:TextView
    lateinit var status:TextView
    lateinit var edit:ImageButton
    lateinit var delete:ImageButton

    fun loadFields(){
        itemView.let {
            name = it.findViewById(R.id.tvListPeriodItem)
            cutOffDay = it.findViewById(R.id.tvListPeriodInterest)
            warningQuote = it.findViewById(R.id.tvListPeriodTotalQuote)
            status = it.findViewById(R.id.tvListPeriodCapital)
            edit =  it.findViewById(R.id.btnEditICC)
            delete = it.findViewById(R.id.btnDeleteItemTCC)
        }
    }


}