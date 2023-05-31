package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListProjectionAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class ListProjectionHolder(val view:View,val navController: NavController): IListHolder<ListProjectionHolder,ProjectionDTO> {
    private lateinit var recycler:RecyclerView
    lateinit var items:TextView
    lateinit var total:TextView
    lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recycler = view.findViewById(R.id.rv_list_lpj)
        items = view.findViewById(R.id.tv_num_items_lpj)
        total = view.findViewById(R.id.tv_tot_saved_lpj)
        progressBar = view.findViewById(R.id.pb_load_lpj)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadFields(fn: ((ListProjectionHolder) -> Unit)?) {
        fn?.invoke(this)
    }

    override fun loadRecycler(data: MutableList<ProjectionDTO>) {
        recycler.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
        ListProjectionAdapter(data,navController).let {
            recycler.adapter = it
        }
        progressBar.visibility = View.GONE
    }

}