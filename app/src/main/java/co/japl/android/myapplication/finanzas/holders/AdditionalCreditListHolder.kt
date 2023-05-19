package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.adapter.ListAdditionalCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal
import java.time.LocalDate

class AdditionalCreditListHolder(val view:View,val navController: NavController) :
    IHolder<AdditionalCreditDTO> {
    private lateinit var numAdditional:TextView
    private lateinit var totAdditional:TextView
    private lateinit var button:MaterialButton
    private lateinit var recyclerView:RecyclerView
    private lateinit var additionalSvc :ISaveSvc<AdditionalCreditDTO>

    override fun setFields(actions: View.OnClickListener?) {
        additionalSvc = AdditionalCreditImpl(ConnectDB(view.context))
        numAdditional = view.findViewById(R.id.tv_num_additionals_al)
        totAdditional = view.findViewById(R.id.tv_tot_additional_al)
        recyclerView = view.findViewById(R.id.rv_list_al)
        button = view.findViewById(R.id.btn_add_al)

        button.setOnClickListener(actions)
    }

    override fun loadFields(values: AdditionalCreditDTO) {
        recyclerView.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
        val list = additionalSvc.get(values)
        val count = list.count()
        val total = list.sumOf { it.value }
        numAdditional.text = count.toString()
        totAdditional.text = NumbersUtil.COPtoString(total)
        ListAdditionalCreditAdapter(list.toMutableList(), navController).let {
            recyclerView.adapter = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): AdditionalCreditDTO {
        return AdditionalCreditDTO(0,"", BigDecimal.ZERO,0, LocalDate.MIN,LocalDate.MAX)
    }

    override fun cleanField() {
    }

    override fun validate(): Boolean {
        return false
    }
}