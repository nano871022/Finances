package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.adapter.ListPeriodPaymentsAdapter
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO

class PeriodCheckPaymentsHolder(val root:View):IListHolder<PeriodCheckPaymentsHolder,PeriodCheckPaymentsPOJO> {
    private lateinit var reciclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        reciclerView = root.findViewById(R.id.rv_periods_pcp)
        progressBar =     root.findViewById(R.id.pb_load_pcp)
        reciclerView.layoutManager = LinearLayoutManager(root.context,
            LinearLayoutManager.VERTICAL,false)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<PeriodCheckPaymentsPOJO>) {
        ListPeriodPaymentsAdapter(data).also {
            reciclerView.adapter = it
        }
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((PeriodCheckPaymentsHolder) -> Unit)?) {
     fn?.invoke(this)
    }
}