package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListBoughtAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.finanzas.holders.ListBoughtHolder
import co.japl.android.myapplication.finanzas.pojo.BoughtRecap
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.holders.view.BoughtViewHolder
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.utils.DateUtils
import java.math.BigDecimal
import java.util.*

class ListBought : Fragment() {

    lateinit var holder:ListBoughtHolder
    lateinit var adapter:RecyclerView.Adapter<BoughtViewHolder>
    lateinit var list:List<CreditCardBoughtDTO>
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SearchSvc<CreditCardBoughtDTO>
    lateinit var creditCard:CreditCard
    lateinit var boughtRecap:BoughtRecap

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_bought, container, false)
        arguments?.let {
            val params = CreditCardQuotesParams.Companion.Historical.download(it)
            creditCard = CreditCard()
            creditCard.codeCreditCard = Optional.ofNullable(params.first)
            creditCard.cutOff = Optional.ofNullable(params.second)
            creditCard.cutoffDay = Optional.ofNullable(params.third)
        }
        connectDB(rootView)
        holder = ListBoughtHolder(rootView)
        holder.setFields(null)
        loadRecyclerView(rootView)
        holder.loadFields(boughtRecap)
        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadRecyclerView(view:View){
        list.let {
            holder.lists {
                it.recyclerView.layoutManager =
                    LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                adapter = ListBoughtAdapter(list.toMutableList(), creditCard.cutOff.get())
                it.recyclerView.adapter = adapter
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectDB(view:View){
        view.context.let {
            dbConnect = ConnectDB(it!!)
        }
        saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        val startDate = DateUtils.startDateFromCutoff(creditCard.cutoffDay.get(),creditCard.cutOff.get())
        val list = saveSvc.getToDate(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val listRecurrent = saveSvc.getRecurrentBuys(creditCard.codeCreditCard.get(),creditCard.cutOff.get())
        val pending = saveSvc.getPendingQuotes(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val joinList = ArrayList<CreditCardBoughtDTO>()
        joinList.addAll(list)
        joinList.addAll(pending)
        joinList.addAll(listRecurrent)
        val capital = saveSvc.getCapital(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val capitalQuotes = saveSvc.getCapitalPendingQuotes(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        Log.d(this.javaClass.name,"Capital: $capital capital Quotes: $capitalQuotes")
        val interest = saveSvc.getInterest(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val interestQuotes = saveSvc.getInterestPendingQuotes(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val pendingToPay = saveSvc.getPendingToPay(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val pendingToPayQuotes = saveSvc.getPendingToPayQuotes(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        if(!this::boughtRecap.isInitialized){
            boughtRecap = BoughtRecap()
        }
        boughtRecap.capitalValue = Optional.ofNullable(capital.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0))))
        boughtRecap.interestValue = Optional.ofNullable(interest.orElse(BigDecimal(0)).plus(interestQuotes.orElse(BigDecimal(0))))
        boughtRecap.pendingToPay = Optional.ofNullable(pendingToPay.orElse(BigDecimal(0)).plus(pendingToPayQuotes.orElse(BigDecimal(0))))
        boughtRecap.totalValue = Optional.ofNullable(boughtRecap.capitalValue.orElse(BigDecimal.ZERO).plus(boughtRecap.interestValue.orElse(BigDecimal.ZERO)))
        boughtRecap.currentValueCapital = capital
        boughtRecap.quotesValueCapital = capitalQuotes
        boughtRecap.currentValueInterest = interest
        boughtRecap.quotesValueInterest = interestQuotes
        this.list = joinList.sortedByDescending { it.boughtDate }
        if( !this::list.isInitialized || this.list.isEmpty()){
            Toast.makeText(view.context,"No hay registros para mostrar", Toast.LENGTH_LONG).show()
            CreditCardQuotesParams.Companion.Historical.toBack(findNavController())
        }
    }
}