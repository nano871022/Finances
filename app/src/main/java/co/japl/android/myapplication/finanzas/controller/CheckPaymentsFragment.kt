package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPaymentsAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckPaymentImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckPaymentSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CheckPaymentsMap
import co.japl.android.myapplication.finanzas.holders.CheckPaymentsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.japl.android.myapplication.finanzas.pojo.mapper.CheckPaymentsMapper
import java.time.LocalDate
import java.util.Optional

class CheckPaymentsFragment : Fragment() , OnClickListener,LoaderManager.LoaderCallbacks<List<CheckPaymentsPOJO>>{
    private lateinit var checkPaymentSvc:ICheckPaymentSvc
    private lateinit var holder:IListHolder<CheckPaymentsHolder,CheckPaymentsPOJO>
    private lateinit var svc:IPaidSvc
    private lateinit var list:MutableList<CheckPaymentsPOJO>
    private lateinit var period:String
    private lateinit var date:LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_check_payments, container, false)
        val connect = ConnectDB(root.context)
        svc = PaidImpl(connect)
        checkPaymentSvc = CheckPaymentImpl(connect)
        holder = CheckPaymentsHolder(root)
        holder.setFields(this)
        period = getPeriod()
        date = LocalDate.now().withDayOfMonth(1).plusMonths(1)
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPeriod():String{
        val date = LocalDate.now()
        val monthValue = date.monthValue
        val month = if(monthValue < 10) "0${monthValue}" else "$monthValue"
        return "${date.year}${month}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_add_cps ->{
                    holder.loadFields {
                        val listPaid = (it.recyclerView.adapter as ListPaymentsAdapter).listPaid
                            listPaid.forEach { checkPaymentSvc.save(CheckPaymentsMap().mapper(it)) }
                        val listPaidRemove = (it.recyclerView.adapter as ListPaymentsAdapter).listPaidRemove
                            listPaidRemove.forEach{checkPaymentSvc.delete(it.id.toInt())}
                        Toast.makeText(context,R.string.success_save_quote_credit,Toast.LENGTH_LONG).show()
                        val sum = listPaid.sumOf { it.value }
                        val paid = list.sumOf { it.value }
                        if(sum == paid){
                            it.btnSave.visibility = View.GONE
                        }
                    }
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<CheckPaymentsPOJO>> {
        return object:AsyncTaskLoader<List<CheckPaymentsPOJO>>(requireContext()){
            private var data:List<CheckPaymentsPOJO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                Log.d(javaClass.name,"onStartingLoading $data")
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<CheckPaymentsPOJO>? {
                val paids = svc.getRecurrent(date)
                list = paids.map { CheckPaymentsMapper().mapper(it,period) }.toMutableList()
                return list
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<CheckPaymentsPOJO>>) {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onLoadFinished(loader: Loader<List<CheckPaymentsPOJO>>, data: List<CheckPaymentsPOJO>?) {
        data?.let{
            val checkPaymentList = it.map { checkPaymentSvc.getCheckPayment(it.codPaid.toInt(),period) }.filter { it.isPresent }.map { it.get() }.toMutableList()
            (holder as CheckPaymentsHolder).set(checkPaymentList)
            holder.loadRecycler(it.toMutableList())
        }
    }

}