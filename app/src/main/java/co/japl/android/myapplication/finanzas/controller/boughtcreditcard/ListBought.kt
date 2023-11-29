package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.common.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.databinding.FragmentListBoughtBinding
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.view.creditcard.bought.BoughtList
import co.japl.android.myapplication.pojo.CreditCard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ListBought : Fragment() , LoaderManager.LoaderCallbacks<BoughtCreditCard>{

    lateinit var creditCard:CreditCard

    @Inject lateinit var saveSvc: IQuoteCreditCardSvc
    @Inject lateinit var taxSvc:ITaxSvc
    @Inject lateinit var boughtListSvc:IBoughtListPort
    @Inject lateinit var creditCardSvc:ICreditCardPort
    @Inject lateinit var differInstallmentSvc:IDifferQuotesPort

    private lateinit var btnAddAdvanceBought: FloatingActionButton
    private lateinit var btnAddQuoteBought: FloatingActionButton

    private var _binding:FragmentListBoughtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBoughtBinding.inflate(inflater, container, false)
        val rootView = binding.root

        btnAddAdvanceBought = rootView.findViewById(R.id.btn_cashadvc_lbcc)
        btnAddQuoteBought = rootView.findViewById(R.id.btn_quote_lbcc)

        btnAddQuoteBought.visibility = View.GONE
        btnAddAdvanceBought.visibility = View.GONE

        arguments?.let {
            val params = CreditCardQuotesParams.Companion.Historical.download(it)
            creditCard = CreditCard()
            creditCard.codeCreditCard = Optional.ofNullable(params.first)
            creditCard.cutOff = Optional.ofNullable(params.second)
            creditCard.cutoffDay = Optional.ofNullable(params.third)
        }

        taxSvc.get(creditCard.codeCreditCard.get().toLong(),creditCard.cutOff.get().monthValue,creditCard.cutOff.get().year,
            TaxEnum.CASH_ADVANCE).takeIf { it.isPresent }?.get()?.let {
            showButtonsCashAdvance()
        }

        taxSvc.get(creditCard.codeCreditCard.get().toLong(),creditCard.cutOff.get().monthValue,creditCard.cutOff.get().year,
            TaxEnum.CREDIT_CARD).takeIf { it.isPresent }?.get()?.let {
            showButtonsQuote()
        }

        btnAddQuoteBought.setOnClickListener {
            CreditCardQuotesParams.Companion.ListBought.newInstanceFloat(0,creditCard.codeCreditCard.get(),findNavController())
        }
        btnAddAdvanceBought.setOnClickListener{
            CashAdvanceParams.newInstanceFloat(creditCard.codeCreditCard.get(),findNavController())
        }

        loaderManager.initLoader(1,null,this)

        return rootView
    }

    private fun showButtonsCashAdvance(){
        btnAddAdvanceBought.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            val animator = ObjectAnimator.ofFloat(btnAddAdvanceBought, "alpha", 1.0F, 0.2F)
            animator.duration = 2000L
            animator.start()
        },5000)
    }
    private fun showButtonsQuote(){
        btnAddQuoteBought.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            val animator = ObjectAnimator.ofFloat(btnAddQuoteBought, "alpha", 1.0F, 0.2F)
            animator.duration = 2000L
            animator.start()
        },5000)
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<BoughtCreditCard> {
        return object: AsyncTaskLoader<BoughtCreditCard>(requireContext()){
            private var data:BoughtCreditCard? = null
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): BoughtCreditCard? {
                val creditCardDto = creditCardSvc.getCreditCard(creditCard.codeCreditCard.get())
                val differ = differInstallmentSvc.getDifferQuote(creditCard.cutOff.get().toLocalDate())
                val boughts = boughtListSvc.getBoughtList(creditCardDto!!,creditCard.cutOff.get())
                val group = boughts.list?.sortedByDescending { it.boughtDate!! }?.groupBy { YearMonth.of(it.boughtDate.year,it.boughtDate.monthValue)!! }!!
                data = BoughtCreditCard(boughts.recap,group,creditCardDto,differ, cutOff = creditCard.cutOff.get())
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

    override fun onLoaderReset(loader: Loader<BoughtCreditCard>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<BoughtCreditCard>, data: BoughtCreditCard?) {
        data?.let {

            view?.findViewById<ProgressBar>(R.id.pb_load_lbcc)?.let {it.visibility = View.GONE}
            binding.boughtListCompose?.apply {
                setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialThemeComposeUI {
                        BoughtList(data)
                    }
                }
            }
        }
    }
}