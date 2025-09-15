package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.ListCreditCardQuoteBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCardQuote : Fragment(){
    @Inject lateinit var creditCardPort: ICreditCardPort
    @Inject lateinit var boughtSvc:IBoughtPort
    @Inject lateinit var creditRateSvc:ITaxPort
    @Inject lateinit var msmSvc: ISMSCreditCardPort
    @Inject lateinit var svc: IBoughtSmsPort
    @Inject lateinit var prefs : Prefs

    lateinit var _binding:ListCreditCardQuoteBinding
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            _binding = ListCreditCardQuoteBinding.inflate(inflater)
            val viewModel = BoughtMonthlyViewModel(
                creditRateSvc,
                creditCardPort,
                boughtSvc,
                navController = findNavController(),
                prefs,
                msmSvc,
                svc
            )
            _binding.cvComposeLccq?.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialThemeComposeUI {
                        BoughtMonthly(viewModel = viewModel)
                    }
                }
            }
            return _binding.root.rootView

    }
}