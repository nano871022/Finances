package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.adapter.ListMonthlyCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import java.math.BigDecimal
import java.time.LocalDate

class MonthlyCreditListFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var credit:CreditDTO
    private lateinit var creditSvc:SaveSvc<CreditDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_monthly_credit_list, container, false)
        val date = CreditFixListParams.downloadMonthly(arguments)
        recycler = root.findViewById(R.id.rv_list_mcl)
        credit = CreditDTO(0,"", LocalDate.MIN,0.0,0, BigDecimal.ZERO, BigDecimal.ZERO,"","")
        creditSvc = CreditFixImpl(ConnectDB(root.context))
        val list = creditSvc.getAll()
        Log.d(javaClass.name,"list credit: $list")
        recycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
        ListMonthlyCreditAdapter(list.toMutableList(),root).let {
            recycler.adapter = it
        }
        return root
    }

}