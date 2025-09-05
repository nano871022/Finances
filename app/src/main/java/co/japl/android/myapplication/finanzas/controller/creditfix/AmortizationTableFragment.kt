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
import co.com.japl.module.credit.controllers.amortization.AmortizationViewModel
import co.com.japl.module.credit.views.amortization.AmortizationScreen
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAmortizationFixTableBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AmortizationTableFragment : Fragment(){

    val viewModel: AmortizationViewModel by viewModels()

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