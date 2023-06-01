package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListAccountAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import com.google.android.material.button.MaterialButton

class AccountListHolder(val root:View):IListHolder<AccountListHolder,AccountDTO> {

    private lateinit var recycler: RecyclerView
    private lateinit var btnAdd: MaterialButton
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        progressBar = root.findViewById(R.id.pb_load_al)
        recycler = root.findViewById(R.id.rv_account_list)
        btnAdd = root.findViewById(R.id.btn_add_al)
        recycler.layoutManager = LinearLayoutManager(root.context,
            LinearLayoutManager.VERTICAL,false)
        btnAdd.setOnClickListener(actions)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<AccountDTO>) {
        ListAccountAdapter(data)?.let {
            recycler.adapter = it
        }
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((AccountListHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}