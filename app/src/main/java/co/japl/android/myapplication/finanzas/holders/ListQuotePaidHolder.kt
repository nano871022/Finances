package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodAdapter
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.putParams.PeriodsParams

class ListQuotePaidHolder(val view: View,val findNavController: NavController):IListHolder<ListQuotePaidHolder,PeriodDTO> {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recyclerView = view.findViewById(R.id.list_period)
        progressBar = view.findViewById(R.id.pb_load_lp)
        recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<PeriodDTO>) {
        if (data.isEmpty()) {
            PeriodsParams.Companion.Historical.toBack(findNavController)
            Toast.makeText(
                view.context,
                view.resources.getString(R.string.there_are_not_data),
                Toast.LENGTH_LONG
            ).show()
        }
        val adapter = ListPeriodAdapter(data , findNavController)
        recyclerView.adapter = adapter
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((ListQuotePaidHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}