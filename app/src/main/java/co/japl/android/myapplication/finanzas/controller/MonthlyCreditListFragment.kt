package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.adapter.ListMonthlyCreditAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.holders.MonthlyCreditListHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDate

class MonthlyCreditListFragment : Fragment() ,LoaderManager.LoaderCallbacks<List<CreditDTO>> {
    private lateinit var credit:CreditDTO
    private lateinit var creditSvc:ICreditFix
    private lateinit var holder:MonthlyCreditListHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_monthly_credit_list, container, false)
        holder = MonthlyCreditListHolder(root)
        val date = getDate()
        creditSvc = CreditFixImpl(ConnectDB(root.context))
        credit = getCredit(date!!)
        holder.setFields(null)
        loaderManager.initLoader(0,null,this)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate():LocalDate{
        val date = CreditFixListParams.downloadMonthly(arguments)
        return date?.let {
            return date.withDayOfMonth(1).plusMonths(1).minusDays(1)
        } ?: LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCredit(date:LocalDate):CreditDTO{
        val id = 0
        val name = ""
        val tax = 0.0
        val period = 0
        val value = BigDecimal.ZERO
        val quote = BigDecimal.ZERO
        val kindOf = ""
        val kindOfTax = ""
        return CreditDTO(id, name ,date,tax,period,value,quote,kindOf,kindOfTax)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<CreditDTO>> {
        return object:AsyncTaskLoader<List<CreditDTO>>(requireContext()){
            private var data:List<CreditDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<CreditDTO>? {
                data = creditSvc.get(credit)
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<CreditDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<CreditDTO>>, data: List<CreditDTO>?) {
        data?.let{holder.loadRecycler(data.toMutableList())}
    }

}