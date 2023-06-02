package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionalItemsCredit
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.format.TextStyle
import java.util.*

class PeriodPaidItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var date:TextView
    lateinit var value:TextView
    lateinit var more:ImageButton
    val items = itemView.context.resources.getStringArray(R.array.period_item_options)

    fun loadFields(){

        view.findViewById<TextView>(R.id.date_paid_ppil)?.let{
            date = it
        }
        view.findViewById<TextView>(R.id.value_ppil)?.let {
            value = it
        }
        view.findViewById<ImageButton>(R.id.btn_more_ppil)?.let {
            more = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:PaidDTO, callback:()->Unit){
        if(this::date.isInitialized) {
            date.text = "${values.date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${values.date.year}"
        }
        if(this::value.isInitialized) {
            value.text = NumbersUtil.toString(values.value)
        }
        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { dialog, which ->
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