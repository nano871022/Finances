package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDate

class MonthlyCreditListFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var credit:CreditDTO
    private lateinit var creditSvc:ICreditFix
    private lateinit var totDebt:TextView
    private lateinit var totQuote:TextView
    private lateinit var numCredit:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_monthly_credit_list, container, false)
        val date = getDate()
        Log.d(javaClass.name,"Date get $date")
        recycler = root.findViewById(R.id.rv_list_mcl)
        totDebt = root.findViewById(R.id.tv_tot_debt_mcl)
        totQuote = root.findViewById(R.id.tv_tot_quote_mcl)
        numCredit = root.findViewById(R.id.tv_num_credits_mcl)
            creditSvc = CreditFixImpl(ConnectDB(root.context))
        credit = getCredit(date!!)
        val list = creditSvc.get(credit)
        val totalDebt = list.sumOf { it.value }
        val totalQuote = list.sumOf { it.quoteValue }
        val numCredits = list.count()
        Log.d(javaClass.name,"list credit: $list")
        recycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
        ListMonthlyCreditAdapter(list.toMutableList(),root).let {
            recycler.adapter = it
        }
        totDebt.text = NumbersUtil.COPtoString(totalDebt)
        totQuote.text = NumbersUtil.COPtoString(totalQuote)
        numCredit.text =  numCredits.toString()

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate():LocalDate{
        val date = CreditFixListParams.downloadMonthly(arguments)
        return date?.let {
            return date.withDayOfMonth(1).plusMonths(1).minusDays(1)
        } ?: LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCredit(date:LocalDate):CreditDTO{
        val id = 0
        val name = ""
        val tax = 0.0
        val period = 0
        val value = BigDecimal.ZERO
        val quote = BigDecimal.ZERO
        val kindOf = ""
        val kindOfTax = ""
        return CreditDTO(id, name ,date,tax,period,value,quote,kindOf,kindOfTax)
    }

}