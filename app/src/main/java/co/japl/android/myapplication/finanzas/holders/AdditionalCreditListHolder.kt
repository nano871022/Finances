package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.adapter.ListAdditionalCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal
import java.time.LocalDate

class AdditionalCreditListHolder(val view:View,val navController: NavController) :IHolder<AdditionalCreditDTO> {
    private lateinit var button:MaterialButton
    private lateinit var recyclerView:RecyclerView
    private lateinit var additionalSvc :ISaveSvc<AdditionalCreditDTO>

    override fun setFields(actions: View.OnClickListener?) {
        additionalSvc = AdditionalCreditImpl(ConnectDB(view.context))

        recyclerView = view.findViewById(R.id.rv_list_al)
        button = view.findViewById(R.id.btn_add_al)

        button.setOnClickListener(actions)
    }

    override fun loadFields(values: AdditionalCreditDTO) {
        recyclerView.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
        Log.d(javaClass.name,"CreditCard: ${values.creditCode}")
        val list = additionalSvc.get(values)

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