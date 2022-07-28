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
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.bussiness.impl.*
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates
import android.content.Context as Context

class ListCreditCardQuote : Fragment(), View.OnClickListener{
    private lateinit var tvRemainderCutOffDate: TextView
    private lateinit var tvCreditCard: TextView
    private lateinit var tvCutOffDateQuote: TextView
    private lateinit var tvTotalValueQuote: TextView
    private lateinit var tvEquityValueQuote: TextView
    private lateinit var tvIterestValueQuote: TextView
    private lateinit var tvNumQuotes: TextView
    private lateinit var tvNumOneQuote: TextView
    private lateinit var tvLastMonthDate: TextView
    private lateinit var tvCapitalQuotesLastMonth: TextView
    private lateinit var tvCapitalQuoteLastMonth: TextView
    private lateinit var tvInteresLastMonth: TextView
    private lateinit var llLastMonth: LinearLayout
    private lateinit var tvTotalQuoteLastMonth: TextView
    private lateinit var btnBoughtHistory:Button
    private lateinit var btnCutOffHistory:Button

    private lateinit var contexto:Context
    private lateinit var btnAddBuy:Button
    private lateinit var btnAddCashAdvance:Button
    private lateinit var btnAddBuyWallet:Button
    private lateinit var btnAddBought:Button
    private lateinit var nameCreaditCard:String
    private lateinit var codeCreaditCard:String
    private lateinit var configSvc: ConfigSvc
    private lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    private lateinit var searchSvc: SearchSvc<CreditCardBoughtDTO>
    private lateinit var cutOff:LocalDateTime
    private lateinit var capital:Optional<BigDecimal>
    private lateinit var capitalQuotes:Optional<BigDecimal>
    private lateinit var capitalQuote:Optional<BigDecimal>
    private lateinit var interes:Optional<BigDecimal>
    private lateinit var interestQuotes:Optional<BigDecimal>
    private var quotes by Delegates.notNull<Long>()
    private var oneQuote by Delegates.notNull<Long>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.list_credit_card_quote, container, false)
        contexto = rootView.context
        configSvc = Config()
        cutOff = configSvc.nextCutOff()
        nameCreaditCard = "Credit Card Name 1"
        codeCreaditCard = "1";
        loadFields(rootView)
        loadData(rootView.context)
        loadDataLastMonth(rootView.context)
        loadValuesOnFields(rootView)
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(context: Context){
        val conecction = ConnectDB(context)
        saveSvc = SaveCreditCardBoughtImpl(conecction)
        searchSvc = saveSvc as SearchSvc<CreditCardBoughtDTO>
        val capital = searchSvc.getCapital(codeCreaditCard.toInt(),cutOff)
        val capitalQuotes = searchSvc.getCapitalPendingQuotes(codeCreaditCard.toInt(),cutOff)
        val interest = searchSvc.getInterest(codeCreaditCard.toInt(),cutOff)
        val interestQuote = searchSvc.getInterestPendingQuotes(codeCreaditCard.toInt(),cutOff)
        val quotes = searchSvc.getBoughtQuotes(codeCreaditCard.toInt(),cutOff)
        val quotesPending = searchSvc.getBoughtPendingQuotes(codeCreaditCard.toInt(),cutOff)
        val oneQuote = searchSvc.getBought(codeCreaditCard.toInt(),cutOff)
        this.capital = Optional.ofNullable(capital.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0))))
        this.interes = Optional.ofNullable(interest.orElse(BigDecimal(0)).plus(interestQuote.orElse(BigDecimal(0))))
        this.quotes = quotes.orElse(0L).plus(quotesPending.orElse(0L))
        this.oneQuote = oneQuote.orElse(0L)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataLastMonth(context: Context){
        val capital = searchSvc.getCapital(codeCreaditCard.toInt(),cutOff.minusMonths(1))
        val capitalQuotes = searchSvc.getCapitalPendingQuotes(codeCreaditCard.toInt(),cutOff.minusMonths(1))
        val interest = searchSvc.getInterest(codeCreaditCard.toInt(),cutOff.minusMonths(1))
        val interestQuote = searchSvc.getInterestPendingQuotes(codeCreaditCard.toInt(),cutOff.minusMonths(1))
        this.capitalQuote = capital
        this.capitalQuotes = capitalQuotes
        this.interestQuotes = Optional.ofNullable(interest.orElse(BigDecimal(0)).plus(interestQuote.orElse(BigDecimal(0))))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View){
     when(view.id){
         R.id.btnAddItem->addBuy()
         R.id.btnBoughtHistory-> boughtHistory()
         R.id.btnCutOffHistory -> cutOffHistory()
     }
    }

    private fun cutOffHistory(){

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun boughtHistory(){
        parentFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListBought()).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
        parentFragmentManager.setFragmentResult("CreditCard", bundleOf("CreditCard" to "$codeCreaditCard|${co.japl.android.myapplication.utils.DateUtils.localDateTimeToString(cutOff)}"))
    }

    private fun addBuy(){
        parentFragmentManager.beginTransaction() .replace(R.id.fragment_initial,QuoteBought()).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
        parentFragmentManager.setFragmentResult("CreditCard", bundleOf("CreditCardName" to "$nameCreaditCard|$codeCreaditCard"))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFields(container: View){

                container.let{
                    tvCreditCard = it.findViewById(R.id.tvCreditCardName)
                    tvCutOffDateQuote = it.findViewById(R.id.tvCutOffDate)!!
                    tvTotalValueQuote = it.findViewById(R.id.tvTotalValueQuote)!!
                    tvEquityValueQuote = it.findViewById(R.id.tvEquityValueQuote)!!
                    tvIterestValueQuote = it.findViewById(R.id.tvInterestValueQuote)!!
                    tvNumOneQuote = it.findViewById(R.id.tvNumOneQuote)
                    tvNumQuotes = it.findViewById(R.id.tvNumQuotes)
                    tvRemainderCutOffDate = it.findViewById(R.id.tvRemainderCutOffDate)
                    btnAddBuy = it.findViewById(R.id.btnAddItem)
                    btnAddBought = it.findViewById(R.id.btnBought)
                    btnAddBuyWallet = it.findViewById(R.id.btnAddBuyWallet)
                    btnAddCashAdvance = it.findViewById(R.id.btnAddCashAdvance)
                    tvCapitalQuotesLastMonth = it.findViewById(R.id.tvCapitalQuotes)
                    tvCapitalQuoteLastMonth = it.findViewById(R.id.tvCapitalQuote)
                    tvInteresLastMonth = it.findViewById(R.id.tvInteresLastMonth)
                    tvLastMonthDate = it.findViewById(R.id.tvLastMonthBought)
                    llLastMonth = it.findViewById(R.id.llLastMonth)
                    tvTotalQuoteLastMonth = it.findViewById(R.id.tvTotalQuoteLastMonth)
                    btnBoughtHistory = it.findViewById(R.id.btnBoughtHistory)
                    btnCutOffHistory = it.findViewById(R.id.btnCutOffHistory)
                }

                btnAddBuy.let {
                    it.setOnClickListener(this)
                }

                btnAddBuyWallet.let {
                    it.visibility = View.INVISIBLE
                }

                btnAddCashAdvance.let {
                    it.visibility = View.INVISIBLE
                }
                btnAddBought.let {
                    it.visibility = View.INVISIBLE
                }
                tvCreditCard.let {
                    it.text = this.nameCreaditCard
                }

        btnBoughtHistory.let{
            it.setOnClickListener(this)
        }

        btnCutOffHistory.let{
            it.setOnClickListener(this)
            it.visibility = View.INVISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadValuesOnFields(container:View){
        if(container != null) {
            tvCutOffDateQuote.text = cutOff.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            if (capital.isPresent and interes.isPresent) {
                tvTotalValueQuote.text = NumbersUtil.COPtoString(capital.get().plus(interes.get()))
            }

            if (capital.isPresent) {
                tvEquityValueQuote.text = NumbersUtil.COPtoString(capital.get())
            } else {
                tvEquityValueQuote.text = "$ 0.00"
            }

            if (interes.isPresent) {
                tvIterestValueQuote.text = NumbersUtil.COPtoString(interes.get())
            } else {
                tvIterestValueQuote.text = "$ 0.00"
            }

            if(quotes != null) {
                tvNumQuotes.text = NumbersUtil.toString(quotes)
            }else{
                tvNumQuotes.text = "00"
            }
            if(oneQuote != null) {
                tvNumOneQuote.text = NumbersUtil.toString(oneQuote)
            }else{
                tvNumOneQuote.text = "00"
            }
            val remainders = cutOff.let{
                val current = LocalDateTime.now()
                if(it.isAfter(current)){
                    val remainder = Period.between(current.toLocalDate(),it.toLocalDate())
                    remainder.days.toLong().toString()
                }else{
                    "00"
                }

            }
            tvRemainderCutOffDate.let{
                it.text = remainders
            }

            tvCapitalQuoteLastMonth.text = NumbersUtil.COPtoString(capitalQuote.orElse(BigDecimal(0)))
            tvCapitalQuotesLastMonth.text = NumbersUtil.COPtoString(capitalQuotes.orElse(BigDecimal(0)))
            tvInteresLastMonth.text = NumbersUtil.COPtoString(interestQuotes.orElse(BigDecimal(0)))
            tvLastMonthDate.text = co.japl.android.myapplication.utils.DateUtils.localDateTimeToString(cutOff.minusMonths(1))
            tvTotalQuoteLastMonth.text = NumbersUtil.COPtoString(capitalQuote.orElse(BigDecimal(0)).plus(capitalQuotes.orElse(BigDecimal(0))).plus(interestQuotes.orElse(BigDecimal(0))))
            if( capitalQuote.isPresent || capitalQuotes.isPresent || interestQuotes.isPresent ){
                llLastMonth.visibility = View.VISIBLE
            }else{
                llLastMonth.visibility = View.INVISIBLE
            }
        }
    }



}