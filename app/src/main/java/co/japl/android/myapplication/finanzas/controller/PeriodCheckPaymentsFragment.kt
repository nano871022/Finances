package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodPaidAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.adapter.ListPeriodPaymentsAdapter
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckPaymentImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckQuoteImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckPaymentSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckQuoteSvc
import co.japl.android.myapplication.finanzas.holders.PeriodCheckPaymentsHolder
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import java.util.*

class PeriodCheckPaymentsFragment : Fragment() , LoaderManager.LoaderCallbacks<List<PeriodCheckPaymentsPOJO>> {
    private lateinit var svc:ICheckPaymentSvc
    private lateinit var quoteSvc:ICheckQuoteSvc
    private lateinit var creditSvc:ICheckCreditSvc
    private lateinit var holder:PeriodCheckPaymentsHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_period_check_payments, container, false)
        val connect = ConnectDB(root.context)
        svc = CheckPaymentImpl(connect)
        quoteSvc = CheckQuoteImpl(connect)
        creditSvc = CheckCreditImpl(connect)

        holder = PeriodCheckPaymentsHolder(root)
        holder.setFields(null)
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<PeriodCheckPaymentsPOJO>> {
        return object:AsyncTaskLoader<List<PeriodCheckPaymentsPOJO>>(requireContext()){
            private var data: MutableList<PeriodCheckPaymentsPOJO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<PeriodCheckPaymentsPOJO>? {
                data = svc.getPeriodsPayment().toMutableList()
                quoteSvc.getPeriodsPayment()?.takeIf { it.isNotEmpty() }?.let{data?.addAll(it)}
                creditSvc.getPeriodsPayment()?.takeIf { it.isNotEmpty() }?.let{data?.addAll(it)}
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<PeriodCheckPaymentsPOJO>>) {
    }

    override fun onLoadFinished(
        loader: Loader<List<PeriodCheckPaymentsPOJO>>,
        data: List<PeriodCheckPaymentsPOJO>?
    ) {
        data?.let{holder.loadRecycler(data.toMutableList())}
    }
}