package co.japl.android.myapplication.finanzas.holders

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.pojo.BoughtRecap
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

class ListBoughtHolder(var view: View, private val inflater: LayoutInflater,val navController: NavController) : IHolder<BoughtRecap>,
    ISpinnerHolder<ListBoughtHolder>, OnClickListener {
    private lateinit var tvCapital: TextView
    private lateinit var tvInterest: TextView
    private lateinit var tvTotalQuote: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnAddAdvanceBought: FloatingActionButton
    private lateinit var btnAddQuoteBought: FloatingActionButton
    private lateinit var scroll:ScrollView
    lateinit var recyclerView: RecyclerView
    lateinit var seeMore: Button
    private lateinit var values: BoughtRecap


    @RequiresApi(Build.VERSION_CODES.M)
    override fun setFields(actions: View.OnClickListener?) {
        tvCapital = view.findViewById(R.id.tvCapitalList)
        tvInterest = view.findViewById(R.id.tvInterestList)
        tvTotalQuote = view.findViewById(R.id.tvTotalQuoteList)
        recyclerView = view.findViewById(R.id.list_bought)
        progressBar = view.findViewById(R.id.pb_load_lbcc)
        seeMore = view.findViewById(R.id.btn_see_more_qccl)
        btnAddAdvanceBought = view.findViewById(R.id.btn_cashadvc_lbcc)
        btnAddQuoteBought = view.findViewById(R.id.btn_quote_lbcc)
        scroll = view.findViewById(R.id.scroll_qccl)
        progressBar.visibility = View.VISIBLE
        seeMore.setOnClickListener(this)
        btnAddQuoteBought.setOnClickListener(this)
        btnAddAdvanceBought.setOnClickListener(this)
        btnAddQuoteBought.visibility = View.GONE
        btnAddAdvanceBought.visibility = View.GONE

   }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: BoughtRecap) {
        this.values = values
        tvCapital.text = NumbersUtil.toString(values.capitalValue.orElse(BigDecimal.ZERO))
        tvInterest.text = NumbersUtil.toString(values.interestValue.orElse(BigDecimal.ZERO))
        tvTotalQuote.text = NumbersUtil.toString(values.totalValue.orElse(BigDecimal.ZERO))
        progressBar.visibility = View.GONE

    }

    fun showButtonsCashAdvance(){
        btnAddAdvanceBought.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            val animator = ObjectAnimator.ofFloat(btnAddAdvanceBought, "alpha", 1.0F, 0.2F)
            animator.duration = 2000L
            animator.start()
        },5000)
    }
    fun showButtonsQuote(){
        btnAddQuoteBought.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            val animator = ObjectAnimator.ofFloat(btnAddQuoteBought, "alpha", 1.0F, 0.2F)
            animator.duration = 2000L
            animator.start()
        },5000)
    }

    override fun downLoadFields(): BoughtRecap {
        return BoughtRecap()
    }

    override fun cleanField() {
    }

    override fun validate(): Boolean {
        return true
    }

    override fun lists(fn: ((ListBoughtHolder) -> Unit)?) {
        fn?.invoke(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(p0: View?) {

        when (p0?.id) {
            R.id.btn_cashadvc_lbcc -> {
                CashAdvanceParams.newInstanceFloat(values.codeCreditCard,navController)
            }
            R.id.btn_quote_lbcc -> {
                CreditCardQuotesParams.Companion.ListBought.newInstanceFloat(0,values.codeCreditCard,navController)
            }

            R.id.btn_see_more_qccl -> {
                if (this::values.isInitialized) {

                    val values = bundleOf(
                        "items" to values.totalItem.orElse(0).toString(),
                        "recurrentItems" to values.recurrentItem.orElse(0).toString(),
                        "quotesItems" to values.quotesItem.orElse(0).toString(),
                        "quoteItems" to values.quoteItem.orElse(0).toString(),
                        "currentCapital" to NumbersUtil.toString(
                            values.currentValueCapital.orElse(
                                BigDecimal.ZERO
                            )
                        ),
                        "currentInterest" to NumbersUtil.toString(
                            values.currentValueInterest.orElse(
                                BigDecimal.ZERO
                            )
                        ),
                        "quoteCapital" to NumbersUtil.toString(
                            values.quotesValueCapital.orElse(
                                BigDecimal.ZERO
                            )
                        ),
                        "quoteInterest" to NumbersUtil.toString(
                            values.quotesValueInterest.orElse(
                                BigDecimal.ZERO
                            )
                        ),
                        "totalCapital" to NumbersUtil.toString(values.capitalValue.orElse(BigDecimal.ZERO)),
                        "totalInterest" to NumbersUtil.toString(
                            values.interestValue.orElse(
                                BigDecimal.ZERO
                            )
                        ),
                        "totalQuote" to NumbersUtil.toString(values.totalValue.orElse(BigDecimal.ZERO)),
                        "pendingValue" to NumbersUtil.toString(values.pendingToPay.orElse(BigDecimal.ZERO))
                    )
                    RecapBoughtCreditCardDialogHolder(view.context, inflater, values).show()
                }
            }
        }
    }
}