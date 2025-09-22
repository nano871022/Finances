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
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.com.japl.module.paid.controllers.period.list.PeriodsViewModel
import co.com.japl.module.paid.views.periods.list.Period
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPeriodsPaidBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.putParams.PeriodPaidParam
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodsPaidFragment : Fragment() {

    @Inject lateinit var service:IPeriodPaidPort

    val viewModel:PeriodsViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = PeriodsViewModel::class.java,
            build = {
                PeriodsViewModel(
                    savedStateHandle=it,
                    paidSvc = service
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  FragmentPeriodsPaidBinding.inflate(inflater)
        root.cvComposeFpp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Period(viewModel = viewModel,findNavController())
                }
            }
        }
        return root.root.rootView
    }

}