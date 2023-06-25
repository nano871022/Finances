package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.holders.CreditFixListHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import java.math.BigDecimal
import java.time.LocalDate

class CreditListFragment : Fragment(), OnClickListener,LoaderManager.LoaderCallbacks<Map<String,BigDecimal>>{
    private lateinit var holder: CreditFixListHolder
    private lateinit var creditList:SaveSvc<CreditDTO>
    private lateinit var date:LocalDate
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_credit_list, container, false)
        creditList = CreditFixImpl(ConnectDB(root.context))
        holder = CreditFixListHolder(root)
        date = LocalDate.now()
        holder.loadField()
        loaderManager.initLoader(0,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_detail_cfl->{
                CreditFixListParams.newInstanceMonthly(date,findNavController())
            }
            R.id.btn_periods_cfl->{
                CreditFixListParams.newInstancePeriod(findNavController())
            }
            R.id.btn_add_cfl->{
                CreditFixListParams.newInstance(findNavController())
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Map<String,BigDecimal>> {
        return object:AsyncTaskLoader<Map<String,BigDecimal>>(requireContext()){
            private val data:Map<String,BigDecimal> ?= null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else {
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): Map<String,BigDecimal>? {
                val svc = creditList as CreditFixImpl

                val quote = svc.getQuoteAll(date)
                val interest = svc.getInterestAll(date)
                val capital = svc.getCapitalAll(date)
                val pending = svc.getPendingToPayAll(date)
                val additional = svc.getAdditionalAll(date)
                return mapOf("quote" to quote,"interest" to interest,"capital" to capital,"pending" to pending,"additional" to additional)
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Map<String,BigDecimal>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<Map<String,BigDecimal>>, data: Map<String,BigDecimal>?) {
        data?.let {
            val quote = it["quote"]?:BigDecimal.ZERO
            val interest = it["interest"]?:BigDecimal.ZERO
            val capital = it["capital"]?:BigDecimal.ZERO
            val additional = it["additional"]?:BigDecimal.ZERO
            val pending = it["pending"]?:BigDecimal.ZERO
            holder.setField(date, quote, interest, capital, pending, additional, this)
        }
    }


}