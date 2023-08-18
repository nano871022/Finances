package co.japl.android.myapplication.finanzas.holders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.adapter.ListMonthlyCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import kotlin.jvm.optionals.toList

class MonthlyCreditListHolder(val root: View,val inflater: LayoutInflater,val navController: NavController):IListHolder<MonthlyCreditListHolder,CreditDTO> {
    private lateinit var recycler: RecyclerView
    private lateinit var totDebt: TextView
    private lateinit var totQuote: TextView
    private lateinit var numCredit: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var additionalSvc: IAdditionalCreditSvc

    override fun setFields(actions: View.OnClickListener?) {
        additionalSvc = AdditionalCreditImpl(ConnectDB(root.context))
        recycler = root.findViewById(R.id.rv_list_mcl)
        totDebt = root.findViewById(R.id.tv_tot_debt_mcl)
        totQuote = root.findViewById(R.id.tv_tot_quote_mcl)
        numCredit = root.findViewById(R.id.tv_num_credits_mcl)
        progressBar = root.findViewById(R.id.pb_load_mcl)
        recycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL,false)
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(list: MutableList<CreditDTO>) {
        val totalDebt = list.sumOf { it.value }
        val totalQuote = list.sumOf { it.quoteValue }
        val numCredits = list.count()
        val additional = list?.map{additionalSvc.get(it.id.toLong())}?.flatMap { it.toList() }?.sumOf { it.value }

        ListMonthlyCreditAdapter(list,root,inflater, navController).let {
            recycler.adapter = it
        }

        totDebt.text = NumbersUtil.toString(totalDebt)
        totQuote.text = NumbersUtil.toString(totalQuote + (additional ?: BigDecimal.ZERO))
        numCredit.text =  numCredits.toString()
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((MonthlyCreditListHolder) -> Unit)?) {
        fn?.invoke(this)
    }

}