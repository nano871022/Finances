package co.japl.android.myapplication.finanzas.controller.creditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSettingSvc
import co.japl.android.myapplication.finanzas.holders.ListCreditCardSettingHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import co.japl.android.myapplication.putParams.ListCreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCardSetting : Fragment() , View.OnClickListener,LoaderManager.LoaderCallbacks<List<CreditCardSettingDTO>> {
    private lateinit var holder: IListHolder<ListCreditCardSettingHolder,CreditCardSettingDTO>
    private var codeCreditCard : Int = 0

    @Inject lateinit var saveSvc: ICreditCardSettingSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_list_credit_card_setting, container, false)
        val map = ListCreditCardSettingParams.download(arguments)
        if(map.containsKey(ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD)) {
            codeCreditCard = map[ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]!!
        }
        holder = ListCreditCardSettingHolder(view,parentFragmentManager,findNavController())
        holder.setFields(this)
        loaderManager.initLoader(1, null, this)
        return view
    }

    private fun add(){
        CreditCardSettingParams.newInstance(codeCreditCard,findNavController())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddNewCCS ->{add()}
            R.id.btnCancelLCCS -> {
                ListCreditCardSettingParams.toBack(codeCreditCard.toString(),findNavController())
            }
            else->{
                view.let{
                Toast.makeText(it!!.context,getString(R.string.toast_invalid_option),Toast.LENGTH_LONG).show()}}
        }
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<CreditCardSettingDTO>> {
        return object:AsyncTaskLoader<List<CreditCardSettingDTO>>(requireContext()){
            private var data: List<CreditCardSettingDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<CreditCardSettingDTO>? {
                data = (saveSvc as CreditCardSettingImpl).getAll(codeCreditCard)
                return data
            }

        }
    }

    override fun onLoaderReset(loader: Loader<List<CreditCardSettingDTO>>) {
    }

    override fun onLoadFinished(
        loader: Loader<List<CreditCardSettingDTO>>,
        data: List<CreditCardSettingDTO>?
    ) {
        data?.let {
            holder.loadRecycler(it.toMutableList())
        }
    }


}