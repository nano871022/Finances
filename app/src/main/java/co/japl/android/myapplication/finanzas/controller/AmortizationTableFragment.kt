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
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.holders.AmortizationTableHolder
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams

class AmortizationTableFragment : Fragment() , LoaderManager.LoaderCallbacks<Triple<CalcDTO, Long,Boolean>> {
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

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Triple<CalcDTO, Long,Boolean>> {
        return object:AsyncTaskLoader<Triple<CalcDTO, Long,Boolean>>(requireContext()){
            var data: Triple<CalcDTO, Long,Boolean>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): Triple<CalcDTO, Long,Boolean>? {
                data = AmortizationTableParams.download(requireArguments())
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Triple<CalcDTO, Long,Boolean>>) {
    }

    override fun onLoadFinished(loader: Loader<Triple<CalcDTO, Long,Boolean>>, data: Triple<CalcDTO, Long,Boolean>?) {
        data?.let {
            holder.setData(data.first)
            holder.add("QuotesPaid", data.second)
            holder.add("quote1NotPaid",data.third)
            holder.create()
            holder.load()
        }
    }


}