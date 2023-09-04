package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.adapter.ExtraValueAdapter
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.AddValueAmortizationConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class ExtraValueListHolder(val view:View): IListHolder<ExtraValueListHolder,Any> {
    private lateinit var listRV:RecyclerView
    private lateinit var btnAdd:Button

    override fun setFields(actions: View.OnClickListener?) {
        listRV = view.findViewById(R.id.rv_extra_value_list_evl)
        btnAdd = view.findViewById(R.id.btn_add_evl)
        btnAdd.setOnClickListener(actions)
        listRV.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
    }

    override fun loadRecycler(data: MutableList<Any>) {
        listRV.adapter = ExtraValueAdapter(data)
    }

    override fun loadFields(fn: ((ExtraValueListHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}