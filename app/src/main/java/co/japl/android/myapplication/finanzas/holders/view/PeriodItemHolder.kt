package co.japl.android.myapplication.holders.view

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

class PeriodItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var period: TextView
    lateinit var interest: TextView
    lateinit var capital: TextView
    lateinit var total: TextView
    lateinit var paid: ImageButton
    lateinit var periodDTO: PeriodDTO

    fun loadFields(){
        period = view.findViewById(R.id.tvListPeriodItem)
        interest = view.findViewById(R.id.tvListPeriodInterest)
        capital = view.findViewById(R.id.tvListPeriodCapital)
        total = view.findViewById(R.id.tvListPeriodTotalQuote)
        paid = view.findViewById(R.id.btnShowPaid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:PeriodDTO, action:View.OnClickListener){
        this.periodDTO =  values
        period.text = values.periodStart.format(DateTimeFormatter.ofPattern("YYYY/MM/dd")).plus(" - ").plus(values.periodEnd.format(DateTimeFormatter.ofPattern("YYYY/MM/dd")))
        interest.text = DecimalFormat("##,###.##").format(values.interest)
        capital.text = DecimalFormat("##,###.##").format(values.capital)
        total.text = DecimalFormat("##,###.##").format(values.total)
        paid.setOnClickListener(action)
    }
}