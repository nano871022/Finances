package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.databinding.FragmentPeriodCheckPaymentsBinding
import co.com.japl.module.paid.views.checkpaids.list.CheckPaids
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodCheckPaymentsFragment : Fragment() {

    @Inject lateinit var checkPaymentSvc:ICheckPaymentPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPeriodCheckPaymentsBinding.inflate( inflater)
       val viewModel = PeriodCheckPaymentViewModel( checkPaymentSvc)
        root.cvComposeFpcp.apply { 
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CheckPaids(viewModel = viewModel)
                }
            }
        }
        return root.root.rootView
    }

}