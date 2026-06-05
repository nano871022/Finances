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
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.com.japl.module.paid.controllers.period.list.PeriodsViewModel
import co.com.japl.module.paid.views.periods.list.Period
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.params.PeriodPaidParam
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodsPaidFragment : Fragment() {

    @Inject lateinit var service:IPeriodPaidPort


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val codeAccount = arguments?.let{PeriodPaidParam().downloadList(it)}
        val viewModel = PeriodsViewModel(
            codeAccount = codeAccount,
            paidSvc = service,
            navController = findNavController()
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Period(viewModel = viewModel)
                }
            }
        }
    }

}
