package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListInputAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton

class InputListHolder(val root: View):IListHolder<InputListHolder,InputDTO> {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: MaterialButton
    private lateinit var numInputs: TextView
    private lateinit var totInputs: TextView
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        numInputs = root.findViewById(R.id.tv_num_inputs_il)
        totInputs = root.findViewById(R.id.tv_tot_inputs_il)
        btnAdd = root.findViewById(R.id.btn_add_il)
        recyclerView = root.findViewById(R.id.rv_input_list)
        progressBar = root.findViewById(R.id.pb_load_il)
        btnAdd.setOnClickListener(actions)
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
    }

    override fun loadRecycler(data: MutableList<InputDTO>) {
        val countInputs = data.count()
        val totalInputs = data.sumOf { it.value }
        numInputs.text = countInputs.toString()
        totInputs.text = NumbersUtil.toString(totalInputs)
        ListInputAdapter(data.toMutableList())?.let {
            recyclerView.adapter = it
        }
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((InputListHolder) -> Unit)?) {
        fn?.invoke(this)
    }

}