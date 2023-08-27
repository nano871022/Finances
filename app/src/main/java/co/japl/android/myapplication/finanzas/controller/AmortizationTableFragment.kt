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
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.bussiness.DTO.DifferInstallmentDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.DifferInstallmentImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IDifferInstallment
import co.japl.android.myapplication.finanzas.holders.AmortizationTableHolder
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class AmortizationTableFragment : Fragment() , LoaderManager.LoaderCallbacks<Map<String,Any>> {
    lateinit var holder: AmortizationTableHolder
    lateinit var differInstallmentSvc: IDifferInstallment
    @RequiresApi(Build.VERSION_CODES.N)
    var differQuote: Optional<DifferInstallmentDTO> =Optional.empty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_amortization_table, container, false)
        differInstallmentSvc = DifferInstallmentImpl(ConnectDB(requireContext()))
        holder = AmortizationTableHolder(view,layoutInflater)
        holder.setup(null)
        loaderManager.initLoader(1, null, this)
        return view
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Map<String,Any>> {
        return object:AsyncTaskLoader<Map<String,Any>>(requireContext()){
            var data: Map<String,Any>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.N)
            override fun loadInBackground(): Map<String,Any>? {
                data = AmortizationTableParams.download(requireArguments())
                data?.takeIf { it[AmortizationTableParams.params.ARG_PARAM_HAS_DIFFER_INSTALLMENT] as Boolean }
                    ?.let{
                        val idBought = it.get(AmortizationTableParams.params.ARG_PARAM_BOUGHT_ID) as Long
                        differQuote = differInstallmentSvc.get(idBought.toInt())
                    }
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Map<String,Any>>) {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onLoadFinished(loader: Loader<Map<String,Any>>, data: Map<String,Any>?) {
        data?.let {
            holder.setData(data[AmortizationTableParams.params.ARG_PARAM_CREDIT_VALUE] as CalcDTO)
            holder.add("QuotesPaid", data[AmortizationTableParams.params.ARG_PARAM_QUOTE_PAID] as Long)
            holder.add("quote1NotPaid",data[AmortizationTableParams.params.ARG_PARAM_QUOTE1_NOT_PAID] as Boolean)
            differQuote?.ifPresent {
                holder.add("differInstallment",differQuote.get())
            }
            holder.add("monthsCalc",data[AmortizationTableParams.params.ARG_PARAM_MONTHS_CALC] as Long)
            holder.create()
            holder.load()
        }
    }


}