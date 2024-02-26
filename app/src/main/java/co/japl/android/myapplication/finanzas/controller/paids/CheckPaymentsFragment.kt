package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPaymentsAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.ICheck
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckPaymentSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckQuoteSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.CheckCreditMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.CheckPaymentsMap
import co.japl.android.myapplication.finanzas.bussiness.mapping.CheckQuoteMap
import co.japl.android.myapplication.finanzas.enums.CheckPaymentsEnum
import co.japl.android.myapplication.finanzas.holders.CheckPaymentsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.japl.android.myapplication.finanzas.pojo.mapper.CheckMapper
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class CheckPaymentsFragment : Fragment() , OnClickListener,LoaderManager.LoaderCallbacks<List<CheckPaymentsPOJO>>{
    private lateinit var holder:IListHolder<CheckPaymentsHolder,CheckPaymentsPOJO>
    private lateinit var list:MutableList<CheckPaymentsPOJO>
    private lateinit var period:String
    private lateinit var date:LocalDate

    @Inject lateinit var svc:IPaidSvc
    @Inject lateinit var creditCardSvc: ICreditCardSvc
    @Inject lateinit var creditsSvc: ICreditFix
    @Inject lateinit var additionalCredirSvc:IAdditionalCreditSvc
    @Inject lateinit var boughtCreditCardSvc: IQuoteCreditCardSvc
    @Inject lateinit var checkPaymentSvc:ICheckPaymentSvc
    @Inject lateinit var checkQuoteSvc:ICheckQuoteSvc
    @Inject lateinit var checkCreditSvc:ICheckCreditSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_check_payments, container, false)
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
                            listPaid.filter { it.type == CheckPaymentsEnum.PAYMENTS }.forEach { checkPaymentSvc.save(CheckPaymentsMap().mapper(it)) }
                            listPaid.filter { it.type == CheckPaymentsEnum.CREDITS }.forEach { checkCreditSvc.save(CheckCreditMap().mapper(it)) }
                            listPaid.filter { it.type == CheckPaymentsEnum.QUOTE_CREDIT_CARD }.forEach { checkQuoteSvc.save(CheckQuoteMap().mapper(it)) }

                        val listRemove = (it.recyclerView.adapter as ListPaymentsAdapter).listPaidRemove
                            listRemove.filter{ it.type == CheckPaymentsEnum.PAYMENTS}.forEach{checkPaymentSvc.delete(it.id.toInt())}
                            listRemove.filter{ it.type == CheckPaymentsEnum.CREDITS}.forEach{checkCreditSvc.delete(it.id.toInt())}
                            listRemove.filter{ it.type == CheckPaymentsEnum.QUOTE_CREDIT_CARD}.forEach{checkQuoteSvc.delete(it.id.toInt())}

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
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<CheckPaymentsPOJO>? {
                list = arrayListOf<CheckPaymentsPOJO>()
                svc.getRecurrent(date)?.takeIf { it.isNotEmpty() }?.map { CheckMapper().mapper(it,period) }?.toMutableList()?.let { list.addAll(it) }

                creditsSvc.getCurrentBoughtCredits(date)
                    ?.takeIf { it.isNotEmpty() }
                    ?.map { CheckMapper().mapper(it,period) }
                    ?.map{
                        additionalCredirSvc.get(it.codPaid.toInt(),date)?.takeIf { it.isNotEmpty() }?.let{list->
                            it.value = it.value + list.sumOf { it.value }
                        }
                        it
                    }
                    ?.toMutableList()?.let { list.addAll(it) }

                boughtCreditCardSvc.getLastAvailableQuotesTC()
                     ?.takeIf { it.isNotEmpty() }?.map { CheckMapper().mapper(it, period) }
                            ?.toMutableList()?.let { list.addAll(it) }

                return list
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<CheckPaymentsPOJO>>) {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onLoadFinished(loader: Loader<List<CheckPaymentsPOJO>>, data: List<CheckPaymentsPOJO>?) {
        data?.let{
            val checks = arrayListOf<ICheck>()
            it.filter{it.type == CheckPaymentsEnum.PAYMENTS}.map { checkPaymentSvc.getCheckPayment(it.codPaid.toInt(),period) }.filter { it.isPresent }.map { it.get() }.toMutableList()
                ?.takeIf { it.isNotEmpty() }?.forEach { checks.add(it as ICheck) }
            it.filter{it.type == CheckPaymentsEnum.QUOTE_CREDIT_CARD}.map { checkQuoteSvc.getCheckPayment(it.codPaid.toInt(),period) }.filter { it.isPresent }.map { it.get() }.toMutableList()
                ?.takeIf { it.isNotEmpty() }?.forEach { checks.add(it as ICheck) }
            it.filter{it.type == CheckPaymentsEnum.CREDITS}.map { checkCreditSvc.getCheckPayment(it.codPaid.toInt(),period) }.filter { it.isPresent }.map { it.get() }.toMutableList()
                ?.takeIf { it.isNotEmpty() }?.forEach { checks.add(it as ICheck) }

            (holder as CheckPaymentsHolder).set(checks)
            holder.loadRecycler(it.toMutableList())
        }
    }

}