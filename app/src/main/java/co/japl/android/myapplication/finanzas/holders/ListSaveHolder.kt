package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListSaveAdapter
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class ListSaveHolder(val view: View):IListHolder<ListSaveHolder,CalcDTO> {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recyclerView = view.findViewById(R.id.list_save)
        progressBar = view.findViewById(R.id.pb_load_ls)
        recyclerView.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<CalcDTO>) {
        val adapter = ListSaveAdapter(data,view)
        recyclerView.adapter = adapter
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((ListSaveHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}