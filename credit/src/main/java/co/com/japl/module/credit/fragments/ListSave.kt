package co.com.japl.module.credit.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.japl.android.myapplication.finanzas.controller.simulators.list.ListSimulator
import co.japl.android.myapplication.finanzas.controller.simulators.list.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.factory.ViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class ListSave : Fragment() {

    @Inject lateinit var simulatorVariableSvc: ISimulatorCreditVariablePort
    @Inject lateinit var simulatorFixSvc: ISimulatorCreditFixPort

    private val viewModel: ListViewModel by viewModels{
        ViewModelFactory (
            owner = this,
            viewModelClass = ListViewModel::class.java,
            build = {
                ListViewModel(
                     requireContext(),
                    simulatorVariableSvc,
                    simulatorFixSvc,
                    navController = findNavController())
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    ListSimulator(viewModel)
                }
              }
        }
    }
}