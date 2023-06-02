package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionalItemsCredit
import co.japl.android.myapplication.utils.DateUtils
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
    fun setFields(values:PaidDTO, callback:()->Unit){
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
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { _, _ ->
                callback.invoke()
            }
        }
        if(this::more.isInitialized) {
            more.setOnClickListener {
                builder.create().show()
            }
        }
    }
}