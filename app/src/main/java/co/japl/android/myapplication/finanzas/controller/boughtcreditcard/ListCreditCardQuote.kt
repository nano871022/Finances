package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.interfaces.*
import co.japl.android.myapplication.bussiness.mapping.CreditCardMap
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.ISpinnerHolder
import co.japl.android.myapplication.holders.QuoteCCHolder
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.android.myapplication.putParams.CreditCardParams
import co.japl.android.myapplication.putParams.TaxesParams
import co.japl.android.myapplication.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCardQuote : Fragment(), LoaderManager.LoaderCallbacks<Pair<CreditCard,List<Pair<String,Double>>>> {
    private lateinit var holder: IHolder<CreditCard>
    private lateinit var listCreditCard: List<CreditCardDTO>
    private lateinit var creditCardDialog: AlertDialog
    private var pojo: CreditCard? = null
    @RequiresApi(Build.VERSION_CODES.N)
    private var currentTaxCC: Optional<TaxDTO> = Optional.empty()

    @Inject
    lateinit var configSvc: ConfigSvc
    @Inject
    lateinit var quoteCreditCardSvc: IQuoteCreditCardSvc
    @Inject
    lateinit var taxSvc: ITaxSvc
    @Inject lateinit var creditCardSvc: ICreditCardSvc

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.list_credit_card_quote, container, false)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        Log.d(this.javaClass.name, ">>> En resume")
        view?.let {
            holder = QuoteCCHolder(it, parentFragmentManager, findNavController(), taxSvc)
            loadFields(it)
        }

        if(creditCardSvc.getAll().isEmpty()){
            notExistCredicardDialog()
        }

        if ((holder as QuoteCCHolder).spCreditCard.text.isNotBlank()) {
            (holder as QuoteCCHolder).progresBar.visibility = View.GONE
            val value = (holder as QuoteCCHolder).spCreditCard.text.toString()
            val creditCard = listCreditCard.firstOrNull { cc -> cc.name == value }
            pojo = creditCard?.let {
                CreditCardMap().mapper(it)
            } ?: CreditCard()
            pojo?.let {
                currentTaxCC = taxSvc.get(
                    it.codeCreditCard.get().toLong(),
                    it.cutOff.get().monthValue,
                    it.cutOff.get().year,
                    TaxEnum.CREDIT_CARD
                )
                if (!currentTaxCC.isPresent) {
                    notExistCurrentTaxDialog()
                } else {
                    LoaderManager.getInstance(this).restartLoader(1, null, this)
                }
            }
        } else {
            (holder as QuoteCCHolder).progresBar.visibility = View.GONE
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(pojo: CreditCard): CreditCard {
        this.context?.let {
            val endDate = pojo.cutOff.get()
            val startDate = DateUtils.startDateFromCutoff(pojo.cutoffDay.get(), endDate)
            val capital =
                quoteCreditCardSvc.getCapital(pojo.codeCreditCard.get(), startDate, endDate)
            val capitalQuotes = quoteCreditCardSvc.getCapitalPendingQuotes(
                pojo.codeCreditCard.get(),
                startDate,
                endDate
            )
            val interest =
                quoteCreditCardSvc.getInterest(pojo.codeCreditCard.get(), startDate, endDate)
            val interestQuote = quoteCreditCardSvc.getInterestPendingQuotes(
                pojo.codeCreditCard.get(),
                startDate,
                endDate
            )
            val quotes =
                quoteCreditCardSvc.getBoughtQuotes(pojo.codeCreditCard.get(), startDate, endDate)
            val quotesPending = quoteCreditCardSvc.getBoughtPendingQuotes(
                pojo.codeCreditCard.get(),
                startDate,
                endDate
            )
            val oneQuote =
                quoteCreditCardSvc.getBought(pojo.codeCreditCard.get(), startDate, endDate)
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
    private fun loadDataLastMonth(pojo: CreditCard): CreditCard {
        val endDate = DateUtils.cutOffLastMonth(pojo.cutoffDay.get())
        val startDate = DateUtils.startDateFromCutoff(pojo.cutoffDay.get(), endDate)
        pojo.cutOffLast = Optional.ofNullable(endDate)
        pojo.capitalQuote =
            quoteCreditCardSvc.getCapital(pojo.codeCreditCard.get(), startDate, endDate)
        pojo.capitalQuotes =
            quoteCreditCardSvc.getCapitalPendingQuotes(
                pojo.codeCreditCard.get(),
                startDate,
                endDate
            )

        val interest = quoteCreditCardSvc.getInterest(pojo.codeCreditCard.get(), startDate, endDate)
        val interestQuote =
            quoteCreditCardSvc.getInterestPendingQuotes(
                pojo.codeCreditCard.get(),
                startDate,
                endDate
            )
        pojo.interestQuotes = Optional.ofNullable(
            interest.orElse(BigDecimal(0)).plus(interestQuote.orElse(BigDecimal(0)))
        )
        return pojo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFields(container: View) {
        val svc = CreditCardImpl(ConnectDB(container.context))
        listCreditCard = svc.getAll()
        holder.setFields(null)
        (holder as ISpinnerHolder<QuoteCCHolder>).lists {
            onItemSelected(it, container)
            if (listCreditCard.isNotEmpty() && listCreditCard.size == 1) {
                (holder as QuoteCCHolder).progresBar.visibility = View.GONE
                val creditCardSel = listCreditCard.first()
                it.spCreditCard.setText(creditCardSel.name)
                pojo = CreditCardMap().mapper(creditCardSel)
            } else {
                holder.cleanField()
                it.spCreditCard.text.clear()
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onItemSelected(holder: QuoteCCHolder, view: View) {
        createDialogCreditCard(view)
        holder.spCreditCard.setOnClickListener { creditCardDialog.show() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDialogCreditCard(view: View) {
        val builder = AlertDialog.Builder(view.context)
        val thisBuild = this
        with(builder) {
            setItems(listCreditCard.map { "${it.id}. ${it.name}" }.toTypedArray()) { _, position ->
                val creditCard = listCreditCard[position]
                holder.cleanField()
                this.context?.let {
                    val now = LocalDateTime.now(ZoneId.systemDefault())
                    (holder as QuoteCCHolder).progresBar.visibility = View.GONE
                    pojo = creditCard?.let {
                        CreditCardMap().mapper(it)
                    } ?: CreditCard()
                    taxSvc.get(creditCard.id.toLong(), now.monthValue, now.year).ifPresent {
                        pojo!!.lastTax = Optional.ofNullable(it.value)
                    }
                    loaderManager.restartLoader(1, null, thisBuild)
                }
            }
        }
        creditCardDialog = builder.create()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataInfo(value: CreditCard): CreditCard? {
        try {
            val pojo = loadData(value)
            return loadDataLastMonth(pojo)
        } catch (e: NoSuchElementException) {
            Log.e(javaClass.name, "$e")
            return null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<Pair<CreditCard, List<Pair<String, Double>>>> {
        Log.d(javaClass.name,"oncreateloader|")
        val value = pojo!!
     return object :
            AsyncTaskLoader<Pair<CreditCard, List<Pair<String, Double>>>>(requireContext()) {
            var data: Pair<CreditCard, List<Pair<String, Double>>>? = null

            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): Pair<CreditCard, List<Pair<String, Double>>>? {
                val dataInf = loadDataInfo(value)
                val list = quoteCreditCardSvc.getDataToGraphStats(
                    value.codeCreditCard.get(),
                    value.cutOff.get()
                )
                data = Pair(dataInf!!, list)
                return data
            }

            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else {
                    forceLoad()
                }
            }
        }.also { it.data = null }
    }

    override fun onLoaderReset(loader: Loader<Pair<CreditCard, List<Pair<String, Double>>>>) {
        Log.d(javaClass.name,"onLoaderReset")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(
        loader: Loader<Pair<CreditCard, List<Pair<String, Double>>>>,
        data: Pair<CreditCard, List<Pair<String, Double>>>?
    ) {
        data?.let {
            holder.loadFields(it.first)
            (holder as QuoteCCHolder)?.let { holder2 ->
                it.second?.takeIf { it.isNotEmpty() }?.let {
                    with(holder2) {
                        cleanPiecePie()
                    }
                    it
                }?.forEach { value ->
                    with(holder2) {
                        loadPiecePie(value.first, value.second)
                    }
                }
            }

        }
    }


    private fun notExistCredicardDialog() {
        val dialog = AlertDialog.Builder(view?.context)
            .setTitle(R.string.title_creditcard_doesnt_create_yet)
            .setMessage(R.string.message_creditcard_doesnt_create_yet)
            .setPositiveButton(R.string.go, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            dialog.cancel()
            CreditCardParams.newInstanceFromQuote(findNavController())
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
            dialog.cancel()
        }
    }

    private fun notExistCurrentTaxDialog() {
        val dialog = AlertDialog.Builder(view?.context)
            .setTitle(R.string.title_tax_doesnt_create_yet)
            .setMessage(R.string.message_tax_doesnt_create_yet)
            .setPositiveButton(R.string.go, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            dialog.cancel()
            TaxesParams.newInstanceFromQCC(findNavController())
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
            dialog.cancel()
        }
    }
}