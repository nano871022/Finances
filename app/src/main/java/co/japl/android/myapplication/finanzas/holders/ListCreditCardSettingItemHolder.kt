package co.japl.android.myapplication.holders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R

class ListCreditCardSettingItemHolder(var itemView:View) : RecyclerView.ViewHolder(itemView)  {
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var type:TextView
    lateinit var creditCard:TextView
    lateinit var status:TextView
    lateinit var edit:ImageButton
    lateinit var delete:ImageButton

    fun loadFields(){
        itemView.let {
            name = it.findViewById(R.id.tvNameLCCS)
            value = it.findViewById(R.id.tvValueLCCS)
            creditCard = it.findViewById(R.id.tvCreditCardLCCS)
            type = it.findViewById(R.id.tvTypeLCCS)
            status = it.findViewById(R.id.tvStatusLCCS)
            edit =  it.findViewById(R.id.btnEditLCCS)
            delete = it.findViewById(R.id.btnDeleteItemLCCS)
        }
    }


}