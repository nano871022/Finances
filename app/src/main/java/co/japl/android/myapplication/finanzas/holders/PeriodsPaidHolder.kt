package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodPaidAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class PeriodsPaidHolder(val root: View,val findNavController: NavController):IListHolder<PeriodsPaidHolder,PaidDTO> {
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recycler = root.findViewById(R.id.rv_periods_ppl)
        progressBar = root.findViewById(R.id.pb_load_ppl)
        recycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<PaidDTO>) {
        ListPeriodPaidAdapter(data,findNavController)?.let{
            recycler.adapter = it
        }
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((PeriodsPaidHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}