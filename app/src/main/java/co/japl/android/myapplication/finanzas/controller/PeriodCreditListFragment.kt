package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.adapter.ListMonthlyCreditAdapter
import co.japl.android.myapplication.finanzas.adapter.ListPeriodCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.holders.PeriodCreditListHolder
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Date

class PeriodCreditListFragment : Fragment() ,LoaderManager.LoaderCallbacks<List<PeriodCreditDTO>>{
    private lateinit var holder: PeriodCreditListHolder
    private lateinit var creditSvc: ICreditFix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_period_credit_list, container, false)
        creditSvc = CreditFixImpl(ConnectDB(root.context))
        holder = PeriodCreditListHolder(root,findNavController())
        holder.setFields(null)
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<PeriodCreditDTO>> {
        return object:AsyncTaskLoader<List<PeriodCreditDTO>>(requireContext()){
            private var data:List<PeriodCreditDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<PeriodCreditDTO>? {
                data = creditSvc.getPeriods()
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<PeriodCreditDTO>>) {
    }

    override fun onLoadFinished(
        loader: Loader<List<PeriodCreditDTO>>,
        data: List<PeriodCreditDTO>?
    ) {
        data?.let{holder.loadRecycler(data.toMutableList())}
    }


}