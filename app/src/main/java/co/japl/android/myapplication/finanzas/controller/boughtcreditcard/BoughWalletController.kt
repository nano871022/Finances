package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.controllers.bought.forms.WalletViewModel
import co.com.japl.module.creditcard.views.bought.forms.Wallet
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.BuyWalletCreditCardBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.putParams.BoughWalletParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BoughWalletController: Fragment() {
    @Inject lateinit var boughtSvc: IBoughtPort
    @Inject lateinit var taxSvc:ITaxPort
    @Inject lateinit var creditCardSvc: ICreditCardPort

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BuyWalletCreditCardBinding.inflate(inflater,container,false)

        val pair = BoughWalletParams.download(requireArguments())
        val viewModel = WalletViewModel(
            codeCreditCard = pair.first,
            codeBought=pair.second?:0,
            period=LocalDateTime.now(),
            creditCardSvc = creditCardSvc,
            boughtSvc=boughtSvc,
            creditRateSvc=taxSvc,
            navController =  findNavController(),
            prefs = ApplicationInitial.prefs)
        binding.cvWalletBwcc.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Wallet(viewModel = viewModel)
                }
            }
        }
        return binding.root.rootView
    }


}