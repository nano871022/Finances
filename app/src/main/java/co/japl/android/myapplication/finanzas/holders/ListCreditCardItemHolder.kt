package co.japl.android.myapplication.holders

import android.app.AlertDialog
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAccount
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsListCreditCard
import co.japl.android.myapplication.utils.NumbersUtil

class ListCreditCardItemHolder(var itemView:View) : RecyclerView.ViewHolder(itemView)  {
    lateinit var name:TextView
    lateinit var cutOffDay:TextView
    lateinit var warningQuote:TextView
    lateinit var status:TextView
    lateinit var more:ImageButton
    val items = itemView.resources.getStringArray(R.array.list_credit_card_items_options)

    fun setFields(){
        itemView.let {
            name = it.findViewById(R.id.tvNameLCCS)
            cutOffDay = it.findViewById(R.id.tvValueLCCS)
            warningQuote = it.findViewById(R.id.tvCreditCardLCCS)
            status = it.findViewById(R.id.tvStatusLCCS)
            more = it.findViewById(R.id.btn_more_ilccs)
        }
    }

    fun loadFields(value: CreditCardDTO,callback:(MoreOptionsItemsListCreditCard)->Unit){
        name.text =value.name
        cutOffDay.text = value.cutOffDay.toString()
        warningQuote.text = NumbersUtil.COPtoString(value.warningValue)
        status.text = value.status.toString()

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(itemView.resources.getString(R.string.pick_option))
            setItems(items) { _, index ->
                callback.invoke(MoreOptionsItemsListCreditCard.values()[index])
            }
        }
        more.setOnClickListener {
            builder.create().show()
        }
    }


}