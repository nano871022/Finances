package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DB.connections.CreditCardBoughtConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.*
import co.japl.android.myapplication.bussiness.interfaces.*
import co.japl.android.myapplication.holders.QuoteCCHolder
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import kotlin.properties.Delegates
import android.content.Context as Context

class ListCreditCardQuote : Fragment(), AdapterView.OnItemSelectedListener{
    private lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    private lateinit var searchSvc: SearchSvc<CreditCardBoughtDTO>
    private lateinit var taxSvc: ITaxSvc
    private lateinit var contexto: Context
    private lateinit var holder:IHolder<CreditCard>
    private lateinit var listCreditCard:List<CreditCardDTO>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.list_credit_card_quote, container, false)
        contexto = rootView.context
        holder = QuoteCCHolder(rootView,parentFragmentManager)
        loadFields(rootView)
         rootView?.context?.let { ConnectDB(it) }?.let {
            taxSvc = TaxImpl(it)
            saveSvc = SaveCreditCardBoughtImpl(it)
            searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
        }!!
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(pojo:CreditCard):CreditCard{
         this.context?.let {
             val capital = searchSvc.getCapital(pojo.codeCreditCard.get(), pojo.cutOff.get())
             val capitalQuotes = searchSvc.getCapitalPendingQuotes(pojo.codeCreditCard.get(), pojo.cutOff.get())
             val interest  = searchSvc.getInterest(pojo.codeCreditCard.get(), pojo.cutOff.get())
             val interestQuote = searchSvc.getInterestPendingQuotes(pojo.codeCreditCard.get(), pojo.cutOff.get())
             val quotes  = searchSvc.getBoughtQuotes(pojo.codeCreditCard.get(), pojo.cutOff.get())
             val quotesPending = searchSvc.getBoughtPendingQuotes(pojo.codeCreditCard.get(), pojo.cutOff.get())
             val oneQuote  = searchSvc.getBought(pojo.codeCreditCard.get(), pojo.cutOff.get())
             pojo.capital = Optional.ofNullable(
                capital.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0)))
            )
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
            pojo.capitalQuote = searchSvc.getCapital(pojo.codeCreditCard.get(), pojo.cutOff.get().minusMonths(1))
            pojo.capitalQuotes =
                searchSvc.getCapitalPendingQuotes(pojo.codeCreditCard.get(), pojo.cutOff.get().minusMonths(1))
            val interest = searchSvc.getInterest(pojo.codeCreditCard.get(), pojo.cutOff.get().minusMonths(1))
            val interestQuote =
                searchSvc.getInterestPendingQuotes(pojo.codeCreditCard.get(), pojo.cutOff.get().minusMonths(1))
            pojo.interestQuotes = Optional.ofNullable(
                interest.orElse(BigDecimal(0)).plus(interestQuote.orElse(BigDecimal(0)))
            )
        return pojo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFields(container: View){
        val svc = CreditCardImpl(ConnectDB(container.context))
        listCreditCard = svc.getAll()
        val list = listCreditCard.stream().map { it.name }.collect(Collectors.toList())
        list.add(0,"-- Seleccionar --")
        holder.setFields(null)
        (holder as ISpinnerHolder<QuoteCCHolder>).lists{
            it.spCreditCard.adapter = ArrayAdapter(it.view.context,R.layout.spinner_simple,R.id.tvValueSp,list)
            it.spCreditCard.onItemSelectedListener = this
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position > 0 )
        this.context?.let {
            val now = LocalDateTime.now()
            val item = parent?.getItemAtPosition(position )
            val creditCard = listCreditCard.firstOrNull { cc -> cc.name == item }
            var pojo = CreditCard()
            creditCard.let {
                pojo.codeCreditCard = Optional.ofNullable(creditCard?.id)
                pojo.nameCreditCard = Optional.ofNullable(creditCard?.name)
                pojo.cutOff =
                Optional.ofNullable(creditCard?.cutOffDay?.toInt()
                    ?.let { it1 -> LocalDateTime.of(now.year, now.month, it1, 0, 0, 0) })
            }
                taxSvc.get(now.monthValue,now.year).ifPresent{
                pojo.lastTax = Optional.ofNullable(it.value)
            }
            pojo = loadData(pojo)
            pojo = loadDataLastMonth(pojo)
            holder.loadFields(pojo)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}