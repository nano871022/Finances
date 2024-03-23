package co.japl.android.myapplication.finanzas.controller.paids

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.controllers.monthly.list.MonthlyViewModel
import co.com.japl.module.paid.views.monthly.list.Monthly
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.databinding.FragmentPaidsBinding
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidsPOJO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAccountSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGraph
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.bussiness.response.GraphValuesResp
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class PaidsFragment : Fragment() {

    @Inject
    lateinit var service: IPaidPort
    @Inject
    lateinit var accountSvc: IAccountPort
    @Inject
    lateinit var incomesSvc: IInputPort


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPaidsBinding.inflate(inflater,container,false)
        val viewModel = MonthlyViewModel(
            paidSvc = service,
            accountSvc = accountSvc,
            incomesSvc = incomesSvc,
            navController = findNavController()
        )
        root.cvPaidsFp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Monthly(viewModel = viewModel)
                }
            }
        }

        return root.root.rootView
    }
}