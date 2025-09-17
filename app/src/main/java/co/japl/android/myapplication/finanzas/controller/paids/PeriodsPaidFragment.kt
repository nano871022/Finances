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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.com.japl.module.paid.controllers.period.list.PeriodsViewModel
import co.com.japl.module.paid.controllers.period.list.PeriodsViewModelFactory
import co.com.japl.module.paid.views.periods.list.Period
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPeriodsPaidBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodsPaidFragment : Fragment() {

    @Inject
    lateinit var service: IPeriodPaidPort

    private val viewModel: PeriodsViewModel by viewModels(
        factoryProducer = {
            PeriodsViewModelFactory(service)
        },
        extrasProducer = {
            val extras = MutableCreationExtras(defaultViewModelCreationExtras)
            extras.apply {
                arguments?.let {
                    set(ViewModelProvider.SavedStateHandleFactory.DEFAULT_ARGS_KEY, it)
                }
            }
            extras
        }
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPeriodsPaidBinding.inflate(inflater)
        root.cvComposeFpp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Period(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return root.root.rootView
    }

}