package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.module.creditcard.controllers.bought.lists.BoughtMonthlyViewModel
import co.com.japl.module.creditcard.views.bought.BoughtMonthly
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import co.japl.android.myapplication.finanzas.ApplicationInitial

@AndroidEntryPoint
class ListCreditCardQuote : Fragment(){
    @Inject lateinit var creditCardPort: ICreditCardPort
    @Inject lateinit var boughtSvc:IBoughtPort
    @Inject lateinit var creditRateSvc:ITaxPort
    @Inject lateinit var msmSvc: ISMSCreditCardPort
    @Inject lateinit var svc: IBoughtSmsPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            val viewModel = BoughtMonthlyViewModel(
                creditRateSvc,
                creditCardPort,
                boughtSvc,
                navController = findNavController(),
                ApplicationInitial.prefs,
                msmSvc,
                svc
            )
            return ComposeView(requireContext()).apply {
                setContent {
                    MaterialThemeComposeUI {
                        BoughtMonthly(viewModel = viewModel)
                    }
                }
            }

    }
}
