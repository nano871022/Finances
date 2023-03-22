package co.japl.android.myapplication.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.putParams.BoughWalletParams
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.putParams.PeriodsParams
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Period
import java.util.*

class QuoteCCHolder(var view:View,var parentFragmentManager:FragmentManager,var navController: NavController,var taxSvc: ITaxSvc): IHolder<CreditCard>, ISpinnerHolder<QuoteCCHolder>, View.OnClickListener{
    lateinit var spCreditCard:MaterialAutoCompleteTextView
    lateinit var tvRemainderCutOffDate: TextView
    lateinit var tvCutOffDateQuote: TextView
    lateinit var tvTotalValueQuote: TextView
    lateinit var tvEquityValueQuote: TextView
    lateinit var tvIterestValueQuote: TextView
    lateinit var tvNumQuotes: TextView
    lateinit var tvNumOneQuote: TextView
    lateinit var tvLastMonthDate: TextView
    lateinit var tvCapitalQuotesLastMonth: TextView
    lateinit var tvCapitalQuoteLastMonth: TextView
    lateinit var tvInteresLastMonth: TextView
    lateinit var llLastMonth: LinearLayout
    lateinit var tvTotalQuoteLastMonth: TextView
    lateinit var btnBoughtHistory: Button
    lateinit var btnCutOffHistory: Button
    lateinit var btnAddBuy: Button
    lateinit var btnAddCashAdvance: Button
    lateinit var btnAddBuyWallet: Button
    lateinit var btnAddBought: Button
    lateinit var nameCreaditCard: Optional<String>
    lateinit var codeCreaditCard:Optional<Int>
    lateinit var cutOff:Optional<LocalDateTime>
    private lateinit var cutOffDay:Optional<Short>

    override fun setFields(actions: View.OnClickListener?) {
        view.let{
            spCreditCard = it.findViewById(R.id.spCreditCardQCC)
            tvCutOffDateQuote = it.findViewById(R.id.tvCutOffDate)!!
            tvTotalValueQuote = it.findViewById(R.id.tvTotalValueQuote)!!
            tvEquityValueQuote = it.findViewById(R.id.tvEquityValueQuote)!!
            tvIterestValueQuote = it.findViewById(R.id.tvInterestValueQuote)!!
            tvNumOneQuote = it.findViewById(R.id.tvNumOneQuote)
            tvNumQuotes = it.findViewById(R.id.tvNumQuotes)
            tvRemainderCutOffDate = it.findViewById(R.id.tvRemainderCutOffDate)
            btnAddBuy = it.findViewById(R.id.btnAddItem)
            btnAddBought = it.findViewById(R.id.btnBought)
            btnAddBuyWallet = it.findViewById(R.id.btnAddBuyWalletLCCQ)
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

        spCreditCard.isFocusable = false

        btnAddBuy.let {
            it.setOnClickListener(this)
            it.visibility = View.GONE
        }

        btnAddBuyWallet.let {
            it.setOnClickListener (this)
            it.visibility = View.GONE
        }

        btnAddCashAdvance.let {
            it.setOnClickListener(this)
            it.visibility = View.GONE
        }
        btnAddBought.let {
            it.visibility = View.GONE
        }

        btnBoughtHistory.let{
            it.setOnClickListener(this)
            it.visibility = View.GONE
        }
        btnCutOffHistory.visibility = View.GONE
        btnCutOffHistory.let{
            it.setOnClickListener(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: CreditCard) {
        view.let {
            val taxAdvance = taxSvc.get(values.codeCreditCard.get().toLong(),values.cutOff.get().monthValue,values.cutOff.get().year,TaxEnum.CASH_ADVANCE)
            val taxCreditCard = taxSvc.get(values.codeCreditCard.get().toLong(),values.cutOff.get().monthValue,values.cutOff.get().year,TaxEnum.CREDIT_CARD)
            val taxWallet = taxSvc.get(values.codeCreditCard.get().toLong(),values.cutOff.get().monthValue,values.cutOff.get().year,TaxEnum.WALLET_BUY)
            codeCreaditCard = values.codeCreditCard
            nameCreaditCard = values.nameCreditCard
            cutOff = values.cutOff
            cutOffDay = values.cutoffDay
            tvCutOffDateQuote.text =  DateUtils.localDateTimeToString(values.cutOff.get())
            if (values.capital.isPresent and values.interest.isPresent) {
                tvTotalValueQuote.text = NumbersUtil.COPtoString(values.capital.get().plus(values.interest.get()))
            }

            if (values.capital.isPresent) {
                tvEquityValueQuote.text = NumbersUtil.COPtoString(values.capital.get())
            } else {
                tvEquityValueQuote.text = "$ 0.00"
            }

            if (values.interest.isPresent) {
                tvIterestValueQuote.text = NumbersUtil.COPtoString(values.interest.get())
            } else {
                tvIterestValueQuote.text = "$ 0.00"
            }

            if(values.quotes.isPresent) {
                tvNumQuotes.text = NumbersUtil.toString(values.quotes.get())
            }else{
                tvNumQuotes.text = "00"
            }
            if(values.oneQuote.isPresent) {
                tvNumOneQuote.text = NumbersUtil.toString(values.oneQuote.get())
            }else{
                tvNumOneQuote.text = "00"
            }
            val remainders = values.cutOff.get().let{
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

            tvCapitalQuoteLastMonth.text = NumbersUtil.COPtoString(values.capitalQuote.orElse(BigDecimal(0)))
            tvCapitalQuotesLastMonth.text = NumbersUtil.COPtoString(values.capitalQuotes.orElse(BigDecimal(0)))
            tvInteresLastMonth.text = NumbersUtil.COPtoString(values.interestQuotes.orElse(BigDecimal(0)))
            tvLastMonthDate.text = DateUtils.localDateTimeToString(values.cutOff.get().minusMonths(1))
            tvTotalQuoteLastMonth.text = NumbersUtil.COPtoString(values.capitalQuote.orElse(BigDecimal(0)).plus(values.capitalQuotes.orElse(
                BigDecimal(0)
            )).plus(values.interestQuotes.orElse(BigDecimal(0))))

            if( values.capitalQuote.isPresent || values.capitalQuotes.isPresent || values.interestQuotes.isPresent ){
                llLastMonth.visibility = View.VISIBLE
            }else{
                llLastMonth.visibility = View.GONE
            }
            Log.d(this.javaClass.name,"TCC:. $taxCreditCard")
            if(taxCreditCard.isPresent) {
                btnAddBuy.visibility = View.VISIBLE
                btnCutOffHistory.visibility = View.VISIBLE
            }
            if(taxAdvance.isPresent) {
                btnAddCashAdvance.visibility = View.VISIBLE
            }
            if(taxWallet.isPresent) {
                btnAddBuyWallet.visibility = View.VISIBLE
            }
            btnBoughtHistory.visibility = View.VISIBLE
        }
    }

    override fun downLoadFields(): CreditCard {
        val pojo =  CreditCard()
        return pojo
    }

    override fun cleanField() {
        btnCutOffHistory.visibility = View.GONE
        btnAddBuyWallet.visibility = View.GONE
        btnAddBuy.visibility = View.GONE
        btnAddBought.visibility = View.GONE
        btnAddCashAdvance.visibility = View.GONE
        btnBoughtHistory.visibility = View.GONE
    }

    override fun validate(): Boolean {
        return true
    }

    override fun lists(fn: ((QuoteCCHolder) -> Unit)?) {
        fn?.invoke(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View){
        Log.d(this.javaClass.name,"btn clicked ${view.id}")
        when(view.id){
            R.id.btnAddBuyWalletLCCQ -> boughWallet()
            R.id.btnAddItem->addBuy()
            R.id.btnBoughtHistory-> boughtHistory()
            R.id.btnCutOffHistory -> cutOffHistory()
            R.id.btnAddCashAdvance -> cashAdvance()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cashAdvance(){
        if(validRedirect()){
            CashAdvanceParams.newInstance(codeCreaditCard.get(),navController)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun boughWallet(){
        Log.d(this.javaClass.name,"before calld to bough Wallet")
        if(validRedirect()){
            Log.d(this.javaClass.name,"redirect to boughWallet")
            BoughWalletParams.newInstance(codeCreaditCard.get(),navController)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cutOffHistory(){
        if(validRedirect()) {
        PeriodsParams.Companion.Historical.newInstance(codeCreaditCard.get(),navController)
        }else{
            Toast.makeText(view.context,"There is not selected any credit card",Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun boughtHistory(){
        if(validRedirect()) {
            CreditCardQuotesParams.Companion.Historical.newInstance(codeCreaditCard.get(), cutOffDay.get(),cutOff.get(), navController)
        }else{
            Toast.makeText(view.context,"There is not selected any credit card",Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun validRedirect():Boolean{
        return this::codeCreaditCard.isInitialized && this::cutOff.isInitialized && codeCreaditCard.isPresent && cutOff.isPresent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBuy(){
        if(validRedirect()) {
            CreditCardQuotesParams.Companion.CreateQuote.newInstance(codeCreaditCard.get(),nameCreaditCard.get(),navController)
        }else{
            Toast.makeText(view.context,"There is not selected any credit card",Toast.LENGTH_LONG).show()
        }
    }


}