package co.japl.android.myapplication.controller

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.Config
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.*
import co.japl.android.myapplication.bussiness.mapping.CreditCardMap
import co.japl.android.myapplication.holders.QuoteCCHolder
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors

class ListCreditCardQuote : Fragment(){
    private lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    private lateinit var searchSvc: SearchSvc<CreditCardBoughtDTO>
    private lateinit var taxSvc: ITaxSvc
    private lateinit var contexto: Context
    private lateinit var holder:IHolder<CreditCard>
    private lateinit var listCreditCard:List<CreditCardDTO>
    private val configSvc:ConfigSvc = Config()
    private lateinit var creditCardDialog:AlertDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.list_credit_card_quote, container, false)
        contexto = rootView.context
        rootView?.context?.let { ConnectDB(it) }?.let {
            taxSvc = TaxImpl(it)
            saveSvc = SaveCreditCardBoughtImpl(it)
            searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
        }!!
        holder = QuoteCCHolder(rootView,parentFragmentManager,findNavController(),taxSvc)
        loadFields(rootView)
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        Log.d(this.javaClass.name,">>> En resume")
        if((holder as QuoteCCHolder).spCreditCard.text.isNotBlank()){
            val value = (holder as QuoteCCHolder).spCreditCard.text.toString()
            val creditCard = listCreditCard.firstOrNull { cc -> cc.name == value }
            val pojo  = creditCard?.let {
                CreditCardMap().mapper(it)
            }?: CreditCard()
            loadDataInfo(pojo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(pojo:CreditCard):CreditCard{
         this.context?.let {
             val endDate = pojo.cutOff.get()
             val startDate = DateUtils.startDateFromCutoff(pojo.cutoffDay.get(),endDate)
             val capital = searchSvc.getCapital(pojo.codeCreditCard.get(), startDate,endDate)
             val capitalQuotes = searchSvc.getCapitalPendingQuotes(pojo.codeCreditCard.get(), startDate,endDate)
             val interest  = searchSvc.getInterest(pojo.codeCreditCard.get(), startDate,endDate)
             val interestQuote = searchSvc.getInterestPendingQuotes(pojo.codeCreditCard.get(), startDate,endDate)
             val quotes  = searchSvc.getBoughtQuotes(pojo.codeCreditCard.get(), startDate,endDate)
             val quotesPending = searchSvc.getBoughtPendingQuotes(pojo.codeCreditCard.get(), startDate,endDate)
             val oneQuote  = searchSvc.getBought(pojo.codeCreditCard.get(), startDate,endDate)
             pojo.capital = Optional.ofNullable(
                capital.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0))))
            pojo.interest = Optional.ofNullable(
                interest.orElse(BigDecimal(0)).plus(interestQuote.orElse(BigDecimal(0)))
            )
            pojo.quotes = Optional.ofNullable(quotes.orElse(0L).plus(quotesPending.orElse(0L)))
            pojo.oneQuote = Optional.ofNullable(oneQuote.orElse(0L))
        }
        return pojo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataLastMonth(pojo:CreditCard):CreditCard{
        val endDate = pojo.cutOff.get().minusMonths(1)
        val startDate = DateUtils.startDateFromCutoff(pojo.cutoffDay.get(),endDate)

        pojo.capitalQuote = searchSvc.getCapital(pojo.codeCreditCard.get(),startDate, endDate)
        pojo.capitalQuotes =
                searchSvc.getCapitalPendingQuotes(pojo.codeCreditCard.get(), startDate,endDate)

        val interest = searchSvc.getInterest(pojo.codeCreditCard.get(), startDate ,endDate)
            val interestQuote =
                searchSvc.getInterestPendingQuotes(pojo.codeCreditCard.get(), startDate,endDate)
            pojo.interestQuotes = Optional.ofNullable(
                interest.orElse(BigDecimal(0)).plus(interestQuote.orElse(BigDecimal(0)))
            )
        return pojo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFields(container: View){
        val svc = CreditCardImpl(ConnectDB(container.context))
        listCreditCard = svc.getAll()
        holder.setFields(null)
        (holder as ISpinnerHolder<QuoteCCHolder>).lists{
            onItemSelected(it,container)
            if(listCreditCard.isNotEmpty() && listCreditCard.size == 1) {
                val creditCardSel = listCreditCard.first()
                it.spCreditCard.setText(creditCardSel.name)
                loadDataInfo(CreditCardMap().mapper(creditCardSel))
            }else{
                holder.cleanField()
                it.spCreditCard.text.clear()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onItemSelected(holder:QuoteCCHolder,view:View) {
        createDialogCreditCard(view)
        holder.spCreditCard.setOnClickListener{ creditCardDialog.show() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDialogCreditCard(view:View){
        val builder = AlertDialog.Builder(view.context)
        with(builder){
            setItems(listCreditCard.map { "${it.id}. ${it.name}" }.toTypedArray()){ _,position ->
                val creditCard = listCreditCard[position]
                holder.cleanField()
                this.context?.let {
                    val now = LocalDateTime.now(ZoneId.systemDefault())
                    Log.d(this.javaClass.name,"Now:. $now")
                    val pojo  = creditCard?.let {
                        CreditCardMap().mapper(it)
                    }?: CreditCard()
                    taxSvc.get(creditCard.id.toLong(),now.monthValue,now.year).ifPresent{
                        pojo.lastTax = Optional.ofNullable(it.value)
                    }
                    loadDataInfo(pojo)
                }
            }
        }
        creditCardDialog = builder.create()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataInfo(value:CreditCard){
        var pojo = loadData(value)
        pojo = loadDataLastMonth(pojo)
        holder.loadFields(pojo)
    }

}