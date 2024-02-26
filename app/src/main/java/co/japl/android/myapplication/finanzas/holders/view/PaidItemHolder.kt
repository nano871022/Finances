package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsPayments
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

class PaidItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var more:AppCompatImageView
    val items = itemView.context.resources.getStringArray(R.array.paids_item_options)

    fun loadFields(){

        view.findViewById<TextView>(R.id.date_paid_pil)?.let{
            date = it
        }
        view.findViewById<TextView>(R.id.name_pil)?.let{
            name = it
        }
        view.findViewById<TextView>(R.id.value_pil)?.let {
            value = it
        }
        view.findViewById<AppCompatImageView>(R.id.btn_more_pil)?.let {
            more = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:PaidDTO, callback:(MoreOptionsItemsPayments)->Unit){
        if(this::date.isInitialized) {
            date.text = DateUtils.localDateToString(values.date)
        }
        if(this::name.isInitialized) {
            name.text = values.name
        }
        if(this::value.isInitialized) {
            value.text = NumbersUtil.toString(values.value)
        }
        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            var itemsShow = items
            if(values.recurrent != (1).toShort()){
                itemsShow = itemsShow.filter { it != items.filterIndexed{index,_ -> index == 0}[0]}.toTypedArray()
                itemsShow = itemsShow.filter { it != items.filterIndexed{index,_ -> index == 1}[0]}.toTypedArray()
            }

            setTitle(view.resources.getString(R.string.pick_option))
            setItems(itemsShow) { _, wich ->
                val item = itemsShow[wich]
                val itemPosition = items.indexOf(item)
                callback.invoke(MoreOptionsItemsPayments.finByPosition(itemPosition))
            }
        }
        if(this::more.isInitialized) {
            more.setOnClickListener {
                builder.create().show()
            }
        }
    }
}