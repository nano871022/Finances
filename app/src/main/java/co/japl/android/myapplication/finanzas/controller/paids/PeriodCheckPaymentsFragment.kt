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
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPeriodCheckPaymentsBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.view.checkpaids.list.CheckPaids
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodCheckPaymentsFragment : Fragment() {

    @Inject lateinit var checkPaymentSvc:ICheckPaymentPort

    val viewModel:PeriodCheckPaymentViewModel by viewModels{
        ViewModelFactory(
            owner=this,
            viewModelClass=PeriodCheckPaymentViewModel::class.java,
            build = {
                PeriodCheckPaymentViewModel(it,checkPaymentSvc)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPeriodCheckPaymentsBinding.inflate( inflater)
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