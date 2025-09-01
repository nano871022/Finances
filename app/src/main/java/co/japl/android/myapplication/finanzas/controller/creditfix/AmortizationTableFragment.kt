package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import co.com.japl.module.credit.controllers.amortization.AmortizationViewModel
import co.com.japl.module.credit.views.amortization.AmortizationScreen
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.databinding.FragmentAmortizationFixTableBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AmortizationTableFragment : Fragment(){

    @Inject lateinit var amortizationSvc: IAmortizationTablePort

    val viewModel: AmortizationViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = AmortizationViewModel::class.java,
            build = {
                val code = arguments?.let{ AmortizationTableParams.download(it)["CREDIT_CODE"] as Long }
                val lastDate = arguments?.let{ AmortizationTableParams.download(it)["LAST_DATE"] as LocalDate }
                AmortizationViewModel(
                    savedStateHandle=it,
                    code = code?.toInt()!!,
                    lastDate = lastDate?:LocalDate.now(),
                    amortizationSvc = amortizationSvc)

            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentAmortizationFixTableBinding.inflate(inflater)
        view.composeViewFaft.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    AmortizationScreen(viewModel)
                }
            }

        }
        return view.root
    }
}