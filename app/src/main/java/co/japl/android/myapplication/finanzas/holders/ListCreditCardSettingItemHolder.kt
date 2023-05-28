package co.japl.android.myapplication.holders

import android.app.AlertDialog
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAccount
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsSettingsCreditCard

class ListCreditCardSettingItemHolder(var itemView:View) : RecyclerView.ViewHolder(itemView)  {
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var type:TextView
    lateinit var creditCard:TextView
    lateinit var status:TextView
    lateinit var more:ImageButton
    val items = itemView.resources.getStringArray(R.array.setting_credit_card_items_options)

    fun setFields(){
        itemView.let {
            name = it.findViewById(R.id.tvNameLCCS)
            value = it.findViewById(R.id.tvValueLCCS)
            creditCard = it.findViewById(R.id.tvCreditCardLCCS)
            type = it.findViewById(R.id.tvTypeLCCS)
            status = it.findViewById(R.id.tvStatusLCCS)
            more =  it.findViewById(R.id.btn_more_ilccs)
        }
    }

    fun loadFields(values: CreditCardSettingDTO,callback:(MoreOptionsItemsSettingsCreditCard)->Unit){
        name.text = values.name
        value.text = values.value
        type.text = values.type
        status.text = if(values.active == 1.toShort()) itemView.resources.getString(R.string.yes) else itemView.resources.getString(R.string.no)

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(itemView.resources.getString(R.string.pick_option))
            setItems(items) { _, index ->
                callback.invoke(MoreOptionsItemsSettingsCreditCard.values()[index])
            }
        }
        more.setOnClickListener {
            builder.create().show()
        }
    }


}