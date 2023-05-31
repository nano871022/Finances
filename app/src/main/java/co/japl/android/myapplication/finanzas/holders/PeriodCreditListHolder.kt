package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.adapter.ListPeriodCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class PeriodCreditListHolder(val root:View,val findNavController: NavController):IListHolder<PeriodCreditListHolder, PeriodCreditDTO> {
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar:ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recycler = root.findViewById(R.id.rv_list_pcl)
        progressBar = root.findViewById(R.id.pb_load_pcl)
        recycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<PeriodCreditDTO>) {
        ListPeriodCreditAdapter(data,root,findNavController).let {
            recycler.adapter = it
        }
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((PeriodCreditListHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}