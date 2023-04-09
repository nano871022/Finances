package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.holders.CreditFixListHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import java.math.BigDecimal
import java.time.LocalDate

class CreditListFragment : Fragment(), OnClickListener{
    private lateinit var holder: CreditFixListHolder
    private lateinit var creditList:SaveSvc<CreditDTO>
    private lateinit var date:LocalDate
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_credit_list, container, false)
        creditList = CreditFixImpl(ConnectDB(root.context))
        holder = CreditFixListHolder(root)
        holder.loadField()
        loadData()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(){
        val svc = creditList as CreditFixImpl
        CreditFixListParams
        date = LocalDate.now()
        val quote = svc.getQuoteAll()
        val interest = svc.getInterestAll(date)
        val capital = svc.getCapitalAll(date)
        val pending = svc.getPendingToPayAll(date)
        val additional = svc.getAdditionalAll()
        holder.setField(date,quote,interest,capital,pending,additional,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_detail_cfl->{
                CreditFixListParams.newInstanceMonthly(date,findNavController())
            }
            R.id.btn_periods_cfl->{
                CreditFixListParams.newInstancePeriod(findNavController())
            }
            R.id.btn_add_cfl->{
                CreditFixListParams.newInstance(findNavController())
            }
        }
    }


}