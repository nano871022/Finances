package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationViewModel
import co.com.japl.module.credit.views.creditamortization.CreditAmortizationScreen
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAmortizationCreditBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AmortizationCreditFragment : Fragment() {

    @Inject lateinit var gracePeriodSvc: IPeriodGracePort
    @Inject lateinit var creditSvc: ICreditPort
    @Inject  lateinit var additionalSvc: IAdditional
    @Inject lateinit var amortizationSvc: IAmortizationTablePort

    val viewModel: CreditAmortizationViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass= CreditAmortizationViewModel::class.java,
            build = {
                CreditAmortizationViewModel(
                    creditCode = 1,
                    lastDate = LocalDate.now(),
                    creditSvc = creditSvc,
                    additionalSvc = additionalSvc,
                    gracePeriodSvc = gracePeriodSvc,
                    amortizationSvc = amortizationSvc,
                    navController = findNavController()
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentAmortizationCreditBinding.inflate( inflater)
        root.composeViewFac.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.Default)
            setContent {
                MaterialThemeComposeUI{
                    CreditAmortizationScreen(viewModel)
                }
            }
        }
        return root.root
    }
}