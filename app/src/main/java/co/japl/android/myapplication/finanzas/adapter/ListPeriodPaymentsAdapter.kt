package co.japl.android.myapplication.finanzas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.view.PeriodPaymentsItemHolder
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO

class ListPeriodPaymentsAdapter(val list:MutableList<PeriodCheckPaymentsPOJO>): RecyclerView.Adapter<PeriodPaymentsItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodPaymentsItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_period_check_payments_item_list,parent,false)
        val holder  = PeriodPaymentsItemHolder(view)
            holder.loadFields()
        return holder
    }

    override fun onBindViewHolder(holder: PeriodPaymentsItemHolder, position: Int) {
        holder.setValues(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}