package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.holders.AmortizationTableHolder
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams

class AmortizationTableFragment : Fragment() , LoaderManager.LoaderCallbacks<Pair<CalcDTO, Long>> {
    lateinit var holder: AmortizationTableHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_amortization_table, container, false)
        holder = AmortizationTableHolder(view)
        holder.setup(null)
        loaderManager.initLoader(1, null, this)
        return view
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Pair<CalcDTO, Long>> {
        return object:AsyncTaskLoader<Pair<CalcDTO, Long>>(requireContext()){
            var data: Pair<CalcDTO, Long>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): Pair<CalcDTO, Long>? {
                data = AmortizationTableParams.download(requireArguments())
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Pair<CalcDTO, Long>>) {
    }

    override fun onLoadFinished(loader: Loader<Pair<CalcDTO, Long>>, data: Pair<CalcDTO, Long>?) {
        data?.let {
            holder.setData(data.first)
            holder.add("QuotesPaid", data.second)
            holder.create()
            holder.load()
        }
    }


}