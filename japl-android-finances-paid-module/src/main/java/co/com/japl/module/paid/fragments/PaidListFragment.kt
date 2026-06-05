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
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.controllers.paid.list.PaidViewModel
import co.com.japl.module.paid.views.paid.list.Paid
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.params.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject
import co.com.japl.ui.Prefs

@AndroidEntryPoint
class PaidListFragment : Fragment()  {

    @Inject lateinit var paidSvc: IPaidPort
    @Inject lateinit var emailSvc:IEmailPaidPort
    @Inject lateinit var paidSmsSvc: ISmsPort



    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val date = PaidsParams.downloadList(requireArguments())
        val codeAccount = PaidsParams.downloadCodeAccount(requireArguments())

        val viewModel = PaidViewModel(
            accountCode = codeAccount?:0,
            period= if(date != null) YearMonth.of(date.year,date.monthValue) else YearMonth.now(),
            paidSvc = paidSvc,
            prefs = Prefs(requireContext().applicationContext),
            navController = findNavController(),
            emailSvc = emailSvc,
            paidSmsSvc = paidSmsSvc
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Paid(viewModel = viewModel)
                }
            }
        }
    }

}
