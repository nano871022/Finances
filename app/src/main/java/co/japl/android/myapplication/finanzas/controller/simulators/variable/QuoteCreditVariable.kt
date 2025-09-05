package co.japl.android.myapplication.finanzas.controller.simulators.variable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.creditcard.controllers.simulator.FormViewModel
import co.com.japl.module.creditcard.views.simulator.Simulator
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.QuoteCreditVariableBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteCreditVariable : Fragment(){

    val viewModel : FormViewModel by viewModels()

    @RequiresApi(value = 31)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = QuoteCreditVariableBinding.inflate(inflater)

        rootView.composeViewSqcv.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Simulator(viewModel, findNavController())
                }
            }
        }
        return rootView.root

    }
}