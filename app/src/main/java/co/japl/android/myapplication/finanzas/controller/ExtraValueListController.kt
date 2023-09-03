package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationQuoteCreditCardDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AddAmortizationImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ExtraValueAmortizationCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ExtraValueAmortizationQuoteCreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAddAmortizationSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IExtraValueAmortizationCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IExtraValueAmortizationQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionesExtraValueItems
import co.japl.android.myapplication.finanzas.holders.ExtraValueListHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.putParams.ExtraValueListParam
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class ExtraValueListController : Fragment(), LoaderManager.LoaderCallbacks<List<Any>>,OnClickListener{
    private lateinit var holder:IListHolder<ExtraValueListHolder,Any>
    private var idCredit:Int = 0
    private lateinit var extraValueKindOf:AmortizationKindOfEnum

    @Inject lateinit var aaSvc:IAddAmortizationSvc
    @Inject lateinit var evcSvc: IExtraValueAmortizationCreditSvc
    @Inject lateinit var exqccSvc: IExtraValueAmortizationQuoteCreditCardSvc

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_extra_value_list, container, false)
        holder = ExtraValueListHolder(root.rootView)
        holder.setFields(this)
        arguments?.let{
            val params = ExtraValueListParam.download(it)
            idCredit = params.first
            extraValueKindOf = params.second
        }
        Log.d(javaClass.name,"extraValueKindOf: $extraValueKindOf")
        LoaderManager.getInstance(this).initLoader(0,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        LoaderManager.getInstance(this).restartLoader(0,null,this)
    }
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Any>> {
        return object: AsyncTaskLoader<List<Any>>(requireContext()){
            private var data:List<Any> ?= null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else {
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<Any>? {
                data = when(extraValueKindOf){
                    AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION -> aaSvc.getAll(idCredit)
                    AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION_CREDIT->  evcSvc.getAll(idCredit)
                    AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION_QUOTE_CREDIT_CARD-> exqccSvc.getAll(idCredit)
                }
                return data
            }

        }
    }

    override fun onLoaderReset(loader: Loader<List<Any>>) {
    }

    override fun onLoadFinished(
        loader: Loader<List<Any>>,
        data: List<Any>?
    ) {
        data?.let {
            holder.loadRecycler(it.toMutableList())
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_add_evl -> AddValueAmortizationDialog(view.context, layoutInflater, idCredit,findNavController()).show()
        }
    }


}