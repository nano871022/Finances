package co.com.japl.module.creditcard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.creditcard.controllers.simulator.FormViewModel
import co.com.japl.module.creditcard.views.simulator.Simulator
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.factory.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuoteCreditVariableFragment : Fragment(){

    @Inject lateinit var quoteCreditScv: ISimulatorCreditVariablePort

    val viewModel : FormViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass=FormViewModel::class.java,
            build={
                FormViewModel(
                    context = requireContext(),
                    savedStateHandler = it,
                    simuladorSvc = quoteCreditScv,
                    navigator = findNavController()
                )
            }
        )
    }

    @RequiresApi(value = 31)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Simulator(viewModel)
                }
            }
        }
    }
}
