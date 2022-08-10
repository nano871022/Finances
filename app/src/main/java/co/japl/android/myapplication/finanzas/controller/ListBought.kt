package co.japl.android.myapplication.controller

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListBoughtAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.holders.view.BoughtViewHolder
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class ListBought : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:RecyclerView.Adapter<BoughtViewHolder>
    lateinit var list:List<CreditCardBoughtDTO>
    lateinit var contexts:Context
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SearchSvc<CreditCardBoughtDTO>
    private lateinit var codeCreditCard:Optional<Int>
    private lateinit var cutOff:LocalDateTime
    private lateinit var tvCapital:TextView
    private lateinit var tvInterest: TextView
    private lateinit var tvPendingToPay: TextView
    private lateinit var tvTotalQuote: TextView
    private lateinit var capital:BigDecimal
    private lateinit var interest: BigDecimal
    private lateinit var pendingToPay:BigDecimal
    private lateinit var totalQuote:BigDecimal


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_bought, container, false)
        contexts = rootView.context
        loadField(rootView)
        return rootView
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("CreditCard", this) { _, bundle ->
            val split =
                bundle.getString("CreditCard").toString().split("|")
            codeCreditCard = Optional.ofNullable(split[0].toString().toInt())
            cutOff = DateUtils.toLocalDateTime(split[1])
            connectDB()
            loadRecyclerView()
            loadDataInFields()
        }
    }

    private fun loadRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(contexts,LinearLayoutManager.VERTICAL,false)
        adapter = ListBoughtAdapter(list,cutOff)
        recyclerView.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectDB(){
        context.let {
            dbConnect = ConnectDB(it!!)
        }
        saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        val list = saveSvc.getToDate(codeCreditCard.get(),cutOff)
        val listRecurrent = saveSvc.getRecurrentBuys(codeCreditCard.get(),cutOff.minusMonths(1))
        val pending = saveSvc.getPendingQuotes(codeCreditCard.get(),cutOff)
        val joinList = ArrayList<CreditCardBoughtDTO>()
        joinList.addAll(list)
        joinList.addAll(pending)
        joinList.addAll(listRecurrent)
        val capital = saveSvc.getCapital(codeCreditCard.get(),cutOff)
        val capitalRecurrent = listRecurrent.filter { it.month == 1 }.map { it.valueItem }.reduceOrNull{ val1,val2->val1.add(val2)}?:BigDecimal.ZERO
        val capitalQuoteRecurrent = listRecurrent.filter { it.month > 1 }.map { it.valueItem.divide(it.month.toBigDecimal()) }.reduceOrNull{val1,val2->val1.add(val2)}?:BigDecimal.ZERO
        val capitalQuotes = saveSvc.getCapitalPendingQuotes(codeCreditCard.get(),cutOff)
        val interest = saveSvc.getInterest(codeCreditCard.get(),cutOff)
        val interestQuotes = saveSvc.getInterestPendingQuotes(codeCreditCard.get(),cutOff)
        val pendingToPay = saveSvc.getPendingToPay(codeCreditCard.get(),cutOff)
        val pendingToPayQuotes = saveSvc.getPendingToPayQuotes(codeCreditCard.get(),cutOff)
        this.capital = capital.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0))).plus(capitalRecurrent).plus(capitalQuoteRecurrent)
        this.interest = interest.orElse(BigDecimal(0)).plus(interestQuotes.orElse(BigDecimal(0)))
        this.pendingToPay = pendingToPay.orElse(BigDecimal(0)).plus(pendingToPayQuotes.orElse(BigDecimal(0)))
        this.totalQuote = this.capital.plus(this.interest)
        this.list = joinList.sortedByDescending { it.boughtDate }
    }

    private fun loadField(container:View){
        container.let {
            tvCapital = it.findViewById(R.id.tvCapitalList)
            tvInterest = it.findViewById(R.id.tvInterestList)
            tvPendingToPay = it.findViewById(R.id.tvPendingToPayList)
            tvTotalQuote = it.findViewById(R.id.tvTotalQuoteList)
            recyclerView = it.findViewById(R.id.list_bought)
        }
    }

    private fun loadDataInFields(){
        tvCapital.text = NumbersUtil.COPtoString(capital)
        tvInterest.text = NumbersUtil.COPtoString(interest)
        tvPendingToPay.text = NumbersUtil.COPtoString(pendingToPay)
        tvTotalQuote.text = NumbersUtil.COPtoString(totalQuote)
    }

}