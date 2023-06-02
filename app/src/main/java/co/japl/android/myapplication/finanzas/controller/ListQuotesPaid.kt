package co.japl.android.myapplication.controller

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodAdapter
import co.japl.android.myapplication.adapter.ListSaveAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGetPeriodsServices
import co.japl.android.myapplication.finanzas.holders.ListQuotePaidHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.putParams.PeriodsParams
import co.japl.android.myapplication.holders.view.PeriodItemHolder
import co.japl.android.myapplication.holders.view.ViewHolder
import java.util.Collections
import kotlin.properties.Delegates

class ListQuotesPaid : Fragment() ,LoaderManager.LoaderCallbacks<List<PeriodDTO>>{
    private lateinit var holder: IListHolder<ListQuotePaidHolder, PeriodDTO>
    lateinit var getPeriodsSvc: IGetPeriodsServices
    var creditCardId by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_periods, container, false)
        arguments?.let{
            creditCardId = PeriodsParams.Companion.Historical.download(it)
        }
        getPeriodsSvc = SaveCreditCardBoughtImpl(ConnectDB(rootView.context))
        holder = ListQuotePaidHolder(rootView,findNavController())
        holder.setFields(null)
        loaderManager.initLoader(1, null, this)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<PeriodDTO>> {
        return object:AsyncTaskLoader<List<PeriodDTO>>(requireContext()){
            private var data: List<PeriodDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<PeriodDTO>? {
                data = getPeriodsSvc.getPeriods(creditCardId)
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<PeriodDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<PeriodDTO>>, data: List<PeriodDTO>?) {
        data?.let {
            holder.loadRecycler(it.toMutableList())
        }
    }


}