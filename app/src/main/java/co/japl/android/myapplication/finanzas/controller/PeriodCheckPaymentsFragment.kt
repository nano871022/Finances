package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodPaidAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.adapter.ListPeriodPaymentsAdapter
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckPaymentImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckPaymentSvc
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import java.util.*

class PeriodCheckPaymentsFragment : Fragment() {
    private lateinit var reciclerView:RecyclerView
    private lateinit var svc:ICheckPaymentSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_period_check_payments, container, false)
        svc = CheckPaymentImpl(ConnectDB(root.context))
        reciclerView = root.findViewById(R.id.rv_periods_pcp)
        val list:MutableList<PeriodCheckPaymentsPOJO> = svc.getPeriodsPayment().toMutableList()
        reciclerView.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        ListPeriodPaymentsAdapter(list).also {
            reciclerView.adapter = it
        }
        return root
    }
}