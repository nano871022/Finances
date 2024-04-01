package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.controllers.paid.list.PaidViewModel
import co.com.japl.module.paid.views.paid.list.Paid
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPaidListBinding
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class PaidListFragment : Fragment()  {

    @Inject lateinit var paidSvc: IPaidPort



    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPaidListBinding.inflate(inflater)
        val date = PaidsParams.downloadList(requireArguments())
        val codeAccount = PaidsParams.downloadCodeAccount(requireArguments())

        val viewModel = PaidViewModel(
            accountCode = codeAccount?:0,
            period= if(date != null) YearMonth.of(date.year,date.monthValue) else YearMonth.now(),
            paidSvc = paidSvc,
            navController = findNavController()
        )
        root.cvPaidFpl.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Paid(viewModel = viewModel)
                }
            }
        }

        return root.root.rootView
    }

}