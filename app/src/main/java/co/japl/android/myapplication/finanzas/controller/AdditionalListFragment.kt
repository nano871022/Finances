package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.holders.AdditionalCreditListHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import java.math.BigDecimal
import java.time.LocalDate

class AdditionalListFragment : Fragment() ,OnClickListener,LoaderManager.LoaderCallbacks<AdditionalCreditDTO>{
    private lateinit var holder: IHolder<AdditionalCreditDTO>
    private var additional : AdditionalCreditDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_additional_list, container, false)
        holder = AdditionalCreditListHolder(root,findNavController())
        holder.setFields(this)
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
         when(view?.id){
            R.id.btn_add_al -> {
                additional?.let {
                    AdditionalCreditParams.newInstance(it, findNavController())
                }
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<AdditionalCreditDTO> {
        return object: AsyncTaskLoader<AdditionalCreditDTO>(requireContext()){
            private var data : AdditionalCreditDTO? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): AdditionalCreditDTO? {
                val creditCode = arguments?.let {
                    CreditFixParams.downloadAdditionalList(it) ?: 0
                } ?: 0
                data = AdditionalCreditDTO(0,"", BigDecimal.ZERO,creditCode, LocalDate.now(), LocalDate.MAX)
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<AdditionalCreditDTO>) {
    }

    override fun onLoadFinished(loader: Loader<AdditionalCreditDTO>, data: AdditionalCreditDTO?) {
        data?.let {
            additional = it
            holder.loadFields(it)
        }
    }

}