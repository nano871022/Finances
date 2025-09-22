package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.controllers.monthly.list.MonthlyViewModel
import co.com.japl.module.paid.views.monthly.list.Monthly
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPaidsBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class PaidsFragment : Fragment() {

    @Inject
    lateinit var service: IPaidPort
    @Inject
    lateinit var accountSvc: IAccountPort
    @Inject
    lateinit var incomesSvc: IInputPort
    @Inject lateinit var paidSmsSvc:ISmsPort
    @Inject lateinit var smsSvc:ISMSPaidPort
    @Inject lateinit var prefs : Prefs

    val viewModel : MonthlyViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass=MonthlyViewModel::class.java,
            build={
                MonthlyViewModel(
                    savedStateHandle = it,
                    paidSvc = service,
                    accountSvc = accountSvc,
                    incomesSvc = incomesSvc,
                    paidSmsSvc = paidSmsSvc,
                    prefs = prefs,
                    smsSvc = smsSvc
                )
            }
        )
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPaidsBinding.inflate(inflater,container,false)
        root.cvPaidsFp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Monthly(viewModel = viewModel,findNavController())
                }
            }
        }

        return root.root.rootView
    }
}