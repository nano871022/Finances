package co.japl.android.myapplication.finanzas.controller.simulators

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import co.japl.android.myapplication.finanzas.view.simulators.ListSimulator
import co.japl.android.myapplication.finanzas.controller.simulators.list.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListSaveBinding
import javax.inject.Inject
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory

@AndroidEntryPoint
class ListSave : Fragment() {
    @Inject lateinit var simulatorVariableSvc: ISimulatorCreditVariablePort
    @Inject lateinit var simulatorFixSvc: ISimulatorCreditFixPort
    private val viewModel: ListViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = ListViewModel::class.java,
            build = {
                ListViewModel(
                    simulatorVariableSvc,
                    simulatorFixSvc
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentListSaveBinding.inflate(inflater)
        root.composeViewFls.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.Default)
            setContent {
                MaterialThemeComposeUI {
                    ListSimulator(viewModel, findNavController())
                }
              }
        }
        return root.root
    }
}
