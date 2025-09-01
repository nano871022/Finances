package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort
import co.com.japl.module.credit.controllers.list.PeriodsViewModel
import co.com.japl.module.credit.views.lists.Periods
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPeriodCreditListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodCreditListFragment : Fragment(){
    @Inject lateinit var periodSvc: IPeriodCreditPort

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPeriodCreditListBinding.inflate(inflater)
        val viewModel = PeriodsViewModel( periodSvc )
        root.composeViewPcl.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Periods(viewModel)
                }
            }
        }
        return root.root
    }
}