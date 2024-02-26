package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.holders.MonthlyCreditListHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MonthlyCreditListFragment : Fragment() ,LoaderManager.LoaderCallbacks<List<CreditDTO>> {
    private lateinit var credit:CreditDTO
    private lateinit var holder:MonthlyCreditListHolder

    @Inject lateinit var creditSvc:ICreditFix
    @Inject lateinit var additionalCredit:IAdditionalCreditSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_monthly_credit_list, container, false)
        holder = MonthlyCreditListHolder(root,layoutInflater,findNavController())
        val date = getDate()
        credit = getCredit(date!!)
        holder.setFields(null)
        loaderManager.initLoader(0,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate():LocalDate{
        val date = CreditFixListParams.downloadMonthly(arguments)
        return (date?.let {
            return date.withDayOfMonth(1).plusMonths(1).minusDays(1)
        } ?: LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1)).also { Log.d(javaClass.name,"Date $it") }
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
                data = creditSvc.get(credit)/*
                val additional = data?.map { additionalCredit.get(it.id.toLong()) }?.flatMap { it.toList() }
                if(additional?.isNotEmpty() == true) {
                    data?.forEach { credit ->
                        val stream = additional?.filter { it.creditCode == credit.id.toLong() }
                            ?.map { it.value }
                       val value = if(stream?.isNotEmpty() == true) {
                            stream?.reduce { acc, bigDecimal -> acc + bigDecimal }
                        } else null
                        credit.quoteValue = credit.quoteValue + (value ?: BigDecimal.ZERO)
                    }
                }*/
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