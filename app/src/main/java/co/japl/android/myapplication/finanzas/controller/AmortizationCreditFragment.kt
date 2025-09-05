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
import co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationViewModel
import co.com.japl.module.credit.views.creditamortization.CreditAmortizationScreen
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAmortizationCreditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AmortizationCreditFragment : Fragment() {

    val viewModel: CreditAmortizationViewModel by viewModels ()

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
                    CreditAmortizationScreen(viewModel, findNavController())
                }
            }
        }
        return root.root
    }
}