package co.japl.android.myapplication.finanzas.controller.creditfix

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
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.response.GraphValuesResp
import co.japl.android.myapplication.finanzas.holders.CreditFixListHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class CreditListFragment : Fragment(), OnClickListener,LoaderManager.LoaderCallbacks<Map<String,Any>>{
    private lateinit var holder: CreditFixListHolder
    private lateinit var date:LocalDate

    @Inject lateinit var creditList:ICreditFix
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_credit_list, container, false)
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

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Map<String,Any>> {
        return object:AsyncTaskLoader<Map<String,Any>>(requireContext()){
            private val data:Map<String,Object> ?= null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else {
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): Map<String, Any> {
                val svc = creditList as CreditFixImpl

                val quote = svc.getQuoteAll(date)
                val interest = svc.getInterestAll(date)
                val capital = svc.getCapitalAll(date)
                val pending = svc.getPendingToPayAll(date)
                val additional = svc.getAdditionalAll(date)
                val graph = svc.getValues(date)
                return mapOf("quote" to quote,"interest" to interest,"capital" to capital,"pending" to pending,"additional" to additional,"graph" to graph)
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Map<String,Any>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<Map<String,Any>>, data: Map<String,Any>?) {
        data?.let {
            val quote = it["quote"] as BigDecimal?:BigDecimal.ZERO
            val interest = it["interest"] as BigDecimal?:BigDecimal.ZERO
            val capital = it["capital"] as BigDecimal?:BigDecimal.ZERO
            val additional = it["additional"] as BigDecimal?:BigDecimal.ZERO
            val pending = it["pending"] as BigDecimal?:BigDecimal.ZERO
            (it["graph"] as List<GraphValuesResp>)?.let {
                holder.execute { holder ->
                    it.forEach {
                        holder.canvas.addPiece(it.name,it.value)
                    }
                    holder.canvas.invalidate()
                }
            }
            holder.setField(date, quote, interest, capital, pending, additional, this)
        }
    }


}