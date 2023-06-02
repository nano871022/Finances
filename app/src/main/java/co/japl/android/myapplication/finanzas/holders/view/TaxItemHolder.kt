package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAccount
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsTax
import co.japl.android.myapplication.finanzas.enums.TaxEnum

class TaxItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var month:TextView
    lateinit var year:TextView
    lateinit var tax:TextView
    lateinit var kind:TextView
    lateinit var period:TextView
    lateinit var layoutKind:LinearLayout
    lateinit var more:ImageButton
    val items = view.resources.getStringArray(R.array.tax_items_options)

    fun loadFields(){
        month = view.findViewById(R.id.tvNameLCCS)
        year = view.findViewById(R.id.tvYearItemTCC)
        tax = view.findViewById(R.id.tvValueItemTCC)
        more = view.findViewById(R.id.btn_more_itccs)
        kind  = view.findViewById(R.id.tvKindTCC)
        period = view.findViewById(R.id.tvPeriodTCC)
        layoutKind = view.findViewById(R.id.lykindTCC)
    }

    fun setFields(values:TaxDTO,callback:(MoreOptionsItemsTax)->Unit){
        val months = view.resources.getStringArray(R.array.Months)
        month.text = months[values.month.toInt()]
        year.text = values.year.toString()
        tax.text = "${values.value} % ${values.kindOfTax ?: "EM"}"
        val kind = TaxEnum.values()[values.kind.toInt()]
        this.kind.text = kind.name
        period.text = values.period.toString()

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { _, index ->
                callback.invoke(MoreOptionsItemsTax.values()[index])
            }
        }
        more.setOnClickListener {
            builder.create().show()
        }

        kind.takeIf { it == TaxEnum.CREDIT_CARD }?.let{
            layoutKind.visibility = View.GONE
        } ?: run{ layoutKind.visibility = View.VISIBLE}
    }
}