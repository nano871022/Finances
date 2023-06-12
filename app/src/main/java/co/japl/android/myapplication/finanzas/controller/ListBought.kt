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
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
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

class ListBought : Fragment() , LoaderManager.LoaderCallbacks<Pair<List<CreditCardBoughtDTO>,BoughtRecap>>{

    lateinit var holder:ListBoughtHolder
    lateinit var adapter:RecyclerView.Adapter<BoughtViewHolder>
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SearchSvc<CreditCardBoughtDTO>
    lateinit var creditCard:CreditCard

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
        rootView.context.let {
            dbConnect = ConnectDB(it!!)
            saveSvc = SaveCreditCardBoughtImpl(dbConnect)
        }
        holder = ListBoughtHolder(rootView)
        holder.setFields(null)
        loaderManager.initLoader(1,null,this)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadRecyclerView(list:List<CreditCardBoughtDTO>){
        if( list.isEmpty()){
            Toast.makeText(requireContext(),"No hay registros para mostrar", Toast.LENGTH_LONG).show()
            CreditCardQuotesParams.Companion.Historical.toBack(findNavController())
        }
        list.let {
            holder.lists {
                it.recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = ListBoughtAdapter(list.toMutableList(), creditCard.cutOff.get())
                it.recyclerView.adapter = adapter
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllData():Pair<List<CreditCardBoughtDTO>,BoughtRecap>{
        val startDate = DateUtils.startDateFromCutoff(creditCard.cutoffDay.get(),creditCard.cutOff.get())
        val list = saveSvc.getToDate(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        val listRecurrent = saveSvc.getRecurrentBuys(creditCard.codeCreditCard.get(),creditCard.cutOff.get())
        val pending = saveSvc.getPendingQuotes(creditCard.codeCreditCard.get(),startDate,creditCard.cutOff.get())
        var joinList = ArrayList<CreditCardBoughtDTO>().toMutableList()
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
        val boughtRecap = BoughtRecap()
        Log.d(javaClass.name,"Interest: $interest + $interestQuotes")
        boughtRecap.capitalValue = Optional.ofNullable(capital.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0))))
        boughtRecap.interestValue = Optional.ofNullable(interest.orElse(BigDecimal(0)).plus(interestQuotes.orElse(BigDecimal(0))))
        boughtRecap.pendingToPay = Optional.ofNullable(pendingToPay.orElse(BigDecimal(0)).plus(pendingToPayQuotes.orElse(BigDecimal(0))))
        boughtRecap.totalValue = Optional.ofNullable(boughtRecap.capitalValue.orElse(BigDecimal.ZERO).plus(boughtRecap.interestValue.orElse(BigDecimal.ZERO)))
        boughtRecap.currentValueCapital = capital
        boughtRecap.quotesValueCapital = capitalQuotes
        boughtRecap.currentValueInterest = interest
        boughtRecap.quotesValueInterest = interestQuotes
        joinList = joinList.sortedByDescending { it.boughtDate }.toMutableList()
        return Pair(joinList,boughtRecap)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Pair<List<CreditCardBoughtDTO>,BoughtRecap>> {
        return object: AsyncTaskLoader<Pair<List<CreditCardBoughtDTO>,BoughtRecap>>(requireContext()){
            private var data:Pair<List<CreditCardBoughtDTO>,BoughtRecap>? = null
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): Pair<List<CreditCardBoughtDTO>,BoughtRecap>? {
                data = getAllData()
                return data
            }

            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Pair<List<CreditCardBoughtDTO>,BoughtRecap>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<Pair<List<CreditCardBoughtDTO>,BoughtRecap>>, data: Pair<List<CreditCardBoughtDTO>,BoughtRecap>?) {
        data?.let {
            loadRecyclerView(it.first)
            holder.loadFields(it.second)
        }
    }
}