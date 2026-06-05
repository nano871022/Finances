package co.com.japl.module.paid.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.controllers.monthly.list.MonthlyViewModel
import co.com.japl.module.paid.views.monthly.list.Monthly
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.params.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject
import co.com.japl.ui.Prefs

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



    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val period = arguments?.let{PaidsParams.downloadPeriod(it)}
        val viewModel = MonthlyViewModel(
            paidSvc = service,
            accountSvc = accountSvc,
            incomesSvc = incomesSvc,
            period = period?:YearMonth.now(),
            paidSmsSvc = paidSmsSvc,
            prefs = Prefs(requireContext().applicationContext),
            smsSvc = smsSvc,
            navController = findNavController()
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Monthly(viewModel = viewModel)
                }
            }
        }
    }
}
