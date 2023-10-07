package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
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
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.holders.ListQuotePaidHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.putParams.PeriodsParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class ListQuotesPaid : Fragment() ,LoaderManager.LoaderCallbacks<List<PeriodDTO>>{
    private lateinit var holder: IListHolder<ListQuotePaidHolder, PeriodDTO>
    var creditCardId by Delegates.notNull<Int>()

    @Inject lateinit var getPeriodsSvc: IQuoteCreditCardSvc

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_periods, container, false)
        arguments?.let{
            creditCardId = PeriodsParams.Companion.Historical.download(it)
        }
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