package co.japl.android.myapplication.finanzas.holders.view

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import co.japl.android.myapplication.utils.NumbersUtil

class PeriodPaymentsItemHolder(val view:View): RecyclerView.ViewHolder(view) {
    lateinit var tvPeriod:TextView
    lateinit var tvPaid:TextView
    lateinit var tvCount:TextView

    fun loadFields(){
        tvPeriod = view.findViewById(R.id.tv_period_pcpi)
        tvPaid = view.findViewById(R.id.tv_paid_pcpi)
        tvCount = view.findViewById(R.id.tv_count_pcpi)
    }

    fun setValues(values:PeriodCheckPaymentsPOJO){
        tvPeriod.text = getPeriodFormat(values.period)
        tvPaid.text = (NumbersUtil.toString(values.paid))
        tvCount.text = values.count.toString()
    }

    fun getPeriodFormat(period:String):String{
        val year = period.substring(0,4)
        val month = period.substring(5)
        return "${view.resources.getStringArray(R.array.Months)[month.toInt()]} $year"
    }

}