package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.com.japl.finances.iports.dtos.RecapDTO
import co.japl.android.myapplication.R
import co.japl.android.myapplication.databinding.FragmentRecapBinding
import co.japl.android.myapplication.finanzas.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.holders.RecapHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.finanzas.view.recap.Recap
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecapFragment @Inject constructor() : Fragment() , LoaderManager.LoaderCallbacks<RecapDTO>{
    private lateinit var holder:IRecapHolder<RecapHolder>
    @Inject lateinit var recapSvc:IRecapPort
    private lateinit var progressBar:ProgressBar

    private var _binding: FragmentRecapBinding? = null
    private val binding get() = _binding!!

    private val recapViewModel by lazy {
        RecapViewModel()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecapBinding.inflate(inflater, container, false)
        val root = binding.root
        progressBar = root.findViewById(R.id.pb_load_rec)
        holder = RecapHolder(root)
        holder.setFields(null)



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoaderManager.getInstance(this).initLoader(1,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<RecapDTO> {
        progressBar.visibility = View.VISIBLE
        return object: AsyncTaskLoader<RecapDTO>(requireContext()){
            private var data : RecapDTO? = null
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): RecapDTO? {
                data =  recapSvc.getTotalValues()
                return data
            }

            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
        }
    }

    override fun onLoadFinished(loader: Loader<RecapDTO>, data: RecapDTO?) {

        binding.firstCardCompose.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                co.com.japl.ui.theme.MaterialThemeComposeUI {
                    Recap(recapViewModel)
                }
            }
        }
        recapViewModel.setProjectionsValue(data?.projectionNext?:0.0)
        recapViewModel.setTotalInbound(data?.totalInputs?:0.0)
        recapViewModel.setTotalPayment(((data?.totalPaid?:0.0) + (data?.totalQuoteCredit?:0.0) + (data?.totalQuoteCreditCard?:0.0)))
        recapViewModel.setTotalPayed(data?.totalPaid?:0.0)
        recapViewModel.setTotalSaved(data?.projectionSaved?:0.0)
        recapViewModel.setTotalCredits(data?.totalQuoteCredit?:0.0)
        recapViewModel.setTotalCreditCard(data?.totalQuoteCreditCard?:0.0)
        recapViewModel.setWarningValue(data?.warningValueCreditCard?:0.0)


        holder.loadFields{
            progressBar.visibility = View.GONE

            it.graph?.let {draw->
                data?.totalQuoteCreditCard?.takeIf { it > 0.0 }?.let{draw.addPiece(view?.resources?.getString(R.string.total_quote_credit_card)!!,it)}
                data?.totalQuoteCredit?.takeIf { it > 0.0 }?.let{draw.addPiece(view?.resources?.getString(R.string.total_credits)!!,it)}
                data?.totalPaid?.takeIf { it > 0.0 }?.let{draw.addPiece(view?.resources?.getString(R.string.total_paids)!!,it)}
                draw.invalidate()
            }
        }
    }

    override fun onLoaderReset(loader: Loader<RecapDTO>) {
    }
}