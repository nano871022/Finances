package co.japl.android.myapplication.holders.view

import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.enums.TaxEnum

class TaxItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var month:TextView
    lateinit var year:TextView
    lateinit var tax:TextView
    lateinit var kind:TextView
    lateinit var period:TextView
    lateinit var layoutKind:LinearLayout
    lateinit var delete:ImageButton

    fun loadFields(){
        month = view.findViewById(R.id.tvNameLCCS)
        year = view.findViewById(R.id.tvYearItemTCC)
        tax = view.findViewById(R.id.tvValueItemTCC)
        delete = view.findViewById(R.id.btnDeleteItemLCCS)
        kind  = view.findViewById(R.id.tvKindTCC)
        period = view.findViewById(R.id.tvPeriodTCC)
        layoutKind = view.findViewById(R.id.lykindTCC)
    }

    fun setFields(values:TaxDTO,action:View.OnClickListener){
        val months = view.resources.getStringArray(R.array.Months)
        month.text = months[values.month.toInt()]
        year.text = values.year.toString()
        tax.text = "${values.value} % ${values.kindOfTax ?: "EM"}"
        val kind = TaxEnum.values()[values.kind.toInt()]
        this.kind.text = kind.name
        period.text = values.period.toString()
        delete.setOnClickListener(action)
        kind.takeIf { it == TaxEnum.CREDIT_CARD }?.let{
            layoutKind.visibility = View.INVISIBLE
        } ?: run{ layoutKind.visibility = View.VISIBLE}
    }
}