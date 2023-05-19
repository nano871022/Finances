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
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPaymentsAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
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

class CheckPaymentsFragment : Fragment() , OnClickListener{
    private lateinit var checkPaymentSvc:ICheckPaymentSvc
    private lateinit var holder:IListHolder<CheckPaymentsHolder,CheckPaymentsPOJO>
    private lateinit var svc:IPaidSvc
    private lateinit var list:MutableList<CheckPaymentsPOJO>

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
        val date = LocalDate.now().withDayOfMonth(1).plusMonths(1)
        val paids = svc.getRecurrent(date)
        val period = getPeriod()
        list = paids.map { CheckPaymentsMapper().mapper(it,period) }.toMutableList()
        val checkPaymentList = list.map { checkPaymentSvc.getCheckPayment(it.codPaid.toInt(),period) }.filter { it.isPresent }.map { it.get() }.toMutableList()
        holder = CheckPaymentsHolder(root,checkPaymentList)
        holder.setFields(this)
        holder.loadRecycler(list)
        return root
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

}