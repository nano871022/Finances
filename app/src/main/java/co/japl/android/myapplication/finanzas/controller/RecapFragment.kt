package co.japl.android.myapplication.finanzas.controller

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.constraintlayout.helper.widget.Carousel
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.holders.RecapHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.card.MaterialCardView
import org.apache.commons.codec.language.Nysiis
import java.math.BigDecimal
import java.time.LocalDate

class RecapFragment : Fragment() , LoaderManager.LoaderCallbacks<Map<String, Any>>{
    private lateinit var holder:IRecapHolder<RecapHolder>
    private lateinit var projectionSvc:IProjectionsSvc
    private lateinit var creditSvc:ICreditFix
    private lateinit var paidSvc:IPaidSvc
    private lateinit var quoteCreditCardSvc:IQuoteCreditCardSvc
    private lateinit var inputSvc:IInputSvc
    private lateinit var creditCardSvc: SaveSvc<CreditCardDTO>
    private lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recap, container, false)
        progressBar = root.findViewById(R.id.pb_load_rec)
        val connectDB =  ConnectDB(root.context)
        projectionSvc = ProjectionsImpl(connectDB,root)
        creditSvc = CreditFixImpl(connectDB)
        paidSvc = PaidImpl(connectDB)
        quoteCreditCardSvc = SaveCreditCardBoughtImpl(connectDB)
        inputSvc = InputImpl(root,connectDB)
        creditCardSvc = CreditCardImpl(connectDB)

        holder = RecapHolder(root)
        holder.setFields(null)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(1,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData():Map<String, Any>{
        val projection = projectionSvc.getTotalSavedAndQuote()
        val totalQuoteCredit = creditSvc.getTotalQuote(LocalDate.now())
        val totalPaid = paidSvc.getTotalPaid()
        val totalQuoteTC = quoteCreditCardSvc.getTotalQuoteTC()
        val totalInputs = inputSvc.getTotalInputs()
        val warning = creditCardSvc.getAll().sumOf { it.warningValue }
        val map = HashMap<String, Any>()
        map["projection"] = projection
        map["totalQuoteCredit"] = totalQuoteCredit
        map["totalPaid"] = totalPaid
        map["totalQuoteTC"] = totalQuoteTC
        map["totalInputs"] = totalInputs
        map["warning"] = warning
        return map
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Map<String, Any>> {
        progressBar.visibility = View.VISIBLE
        return object: AsyncTaskLoader<Map<String,Any>>(requireContext()){
            private var data : Map<String,Any> = HashMap<String,Any>()
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): Map<String, Any>? {
                data =  loadData()
                return data
            }

            override fun onStartLoading() {
                super.onStartLoading()
                if(data.isNotEmpty()){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Map<String, Any>>, data: Map<String, Any>?) {
        val projection = data?.get("projection") as Pair<BigDecimal,BigDecimal>
        val totalQuoteCredit = data?.get("totalQuoteCredit") as BigDecimal
        val totalPaid = data?.get("totalPaid") as BigDecimal
        val totalQuoteTC = data?.get("totalQuoteTC") as BigDecimal
        val totalInputs = data?.get("totalInputs") as BigDecimal
        val warning = data?.get("warning") as BigDecimal

        holder.loadFields{
            it.inputs.text = NumbersUtil.toString(totalInputs)
            it.quoteTC.text = NumbersUtil.toString(totalQuoteTC)
            it.totalCredits.text = NumbersUtil.toString(totalQuoteCredit)
            it.saved.text = NumbersUtil.toString(projection.first)
            it.quoteSaved.text = NumbersUtil.toString(projection.second)
            it.totalPaid.text = NumbersUtil.toString(totalPaid)

            it.totalFix.text = NumbersUtil.toString(totalPaid + totalQuoteCredit)
            val inputFix = totalInputs - (totalPaid + totalQuoteCredit)
            it.totalInputFix.text = NumbersUtil.toString(inputFix)
            if(inputFix < BigDecimal.ZERO){
                it.totalInputFix.setTextColor(Color.RED)
            }
            it.totalPaids.text = NumbersUtil.toString(totalPaid + totalQuoteCredit + totalQuoteTC)
            val totalPaids= totalInputs - (totalPaid + totalQuoteCredit + totalQuoteTC)
            it.totalInputsPaids.text = NumbersUtil.toString( totalPaids)
            if(totalPaids < BigDecimal.ZERO){
                it.totalInputsPaids.setTextColor(Color.RED)
            }
            it.warning.text = NumbersUtil.toString(warning)
            val limit = warning - totalQuoteTC
            it.limit.text = NumbersUtil.toString(limit)
            if(limit < BigDecimal.ZERO){
                it.limit.setTextColor(Color.RED)
            }
            progressBar.visibility = View.GONE
        }
    }

    override fun onLoaderReset(loader: Loader<Map<String, Any>>) {
    }
}