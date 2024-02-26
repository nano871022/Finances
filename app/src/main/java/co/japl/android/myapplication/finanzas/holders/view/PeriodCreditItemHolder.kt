package co.japl.android.myapplication.finanzas.holders.view

import android.app.AlertDialog
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import co.japl.android.myapplication.finanzas.putParams.PeriodCreditListParams
import co.japl.android.myapplication.utils.NumbersUtil

class PeriodCreditItemHolder(val view:View,val navController: NavController):ViewHolder(view) {
    lateinit var date:TextView
    lateinit var count:TextView
    lateinit var value: TextView
    lateinit var more:ImageView
    val items = itemView.context.resources.getStringArray(R.array.period_item_options)

    fun loadField(){
        date = view.findViewById(R.id.month_year_pcil)
        count = view.findViewById(R.id.num_credits_pcil)
        value = view.findViewById(R.id.value_pcil)
        more = view.findViewById(R.id.btn_more_pcil)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setField(values:PeriodCreditDTO){
        val month = view.resources.getStringArray(R.array.Months)
        date.text = "${month[values.date.monthValue]} ${values.date.year}"
        count.text = values.count.toString()
        value.text = NumbersUtil.toString(values.value)

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { dialog, which ->
                PeriodCreditListParams.newInstance(values.date, navController)
            }
        }
        more.setOnClickListener{
            builder.create().show()
        }
    }
}