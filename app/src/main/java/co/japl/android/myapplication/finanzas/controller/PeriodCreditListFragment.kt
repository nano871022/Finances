package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.adapter.ListMonthlyCreditAdapter
import co.japl.android.myapplication.finanzas.adapter.ListPeriodCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Date

class PeriodCreditListFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var credit: CreditDTO
    private lateinit var creditSvc: ICreditFix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_period_credit_list, container, false)
        creditSvc = CreditFixImpl(ConnectDB(root.context))
        recycler = root.findViewById(R.id.rv_list_pcl)
        recycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
        ListPeriodCreditAdapter(getPeriods(),root,findNavController()).let {
            recycler.adapter = it
        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPeriods():MutableList<PeriodCreditDTO>{
        return creditSvc.getPeriods().toMutableList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPeriod(date:String, count:Int, value:BigDecimal):PeriodCreditDTO{
        val year = date.substring(0,4).toInt()
        val month = date.substring(5).toInt()
        val date = LocalDate.of(year,month,1)
        return PeriodCreditDTO(date,count,value)
    }
}