package co.japl.android.myapplication.finanzas.controller.simulators.fix

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
import co.com.japl.module.credit.controllers.simulator.SimulatorFixViewModel
import co.com.japl.module.credit.views.simulator.Simulator
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSimulatorCreditBinding
import dagger.hilt.android.AndroidEntryPoint
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import javax.inject.Inject

@AndroidEntryPoint
class SimulatorCredit : Fragment(){
    @Inject lateinit var simulatorSvc : ISimulatorCreditFixPort
    val viewModel: SimulatorFixViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = SimulatorFixViewModel::class.java,
            build = {
                SimulatorFixViewModel(
                    context = requireContext(),
                    simuladorSvc = simulatorSvc,
                    savedStateHandler = it,
                    navigator = findNavController()
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = FragmentSimulatorCreditBinding.inflate(inflater,container,false)
        rootView.composeViewSqc.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Simulator(viewModel,findNavController())
                }
            }
        }
        return rootView.root

    }
}
