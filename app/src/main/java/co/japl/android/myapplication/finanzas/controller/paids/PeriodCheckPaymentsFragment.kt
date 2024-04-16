package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import co.com.japl.finances.iports.inbounds.paid.ICheckPaymentPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPeriodCheckPaymentsBinding
import co.japl.android.myapplication.finanzas.view.checkpaids.list.CheckPaids
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodCheckPaymentsFragment : Fragment() {

    @Inject lateinit var paidSvc:ICheckPaymentPort
    @Inject lateinit var quoteSvc:co.com.japl.finances.iports.inbounds.creditcard.bought.ICheckPaymentPort
    @Inject lateinit var creditSvc:co.com.japl.finances.iports.inbounds.credit.ICheckPaymentPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPeriodCheckPaymentsBinding.inflate( inflater)
       val viewModel = PeriodCheckPaymentViewModel( paidPeriodCheck = paidSvc,creditPeriodCheck = creditSvc,creditCardPeriodCheck=quoteSvc)
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