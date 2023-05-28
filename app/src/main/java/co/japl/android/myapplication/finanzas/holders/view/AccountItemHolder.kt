package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAccount

class AccountItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var name:TextView
    lateinit var more:AppCompatImageView
    val items = view.resources.getStringArray(R.array.account_items_options)

    fun loadFields(){
        name = view.findViewById(R.id.name_accil)
        more = view.findViewById(R.id.btn_more_accil)
    }

    fun setFields(values:AccountDTO,callback:(MoreOptionsItemsAccount)->Unit){
        name.text = values.name

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { _, index ->
                callback.invoke(MoreOptionsItemsAccount.values()[index])
            }
        }
        more.setOnClickListener {
            builder.create().show()
        }
    }
}