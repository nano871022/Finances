package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.controllers.bought.forms.WalletViewModel
import co.com.japl.module.creditcard.views.bought.forms.Wallet
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.factory.ViewModelFactory
import co.com.japl.module.creditcard.params.BoughWalletParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject
import co.japl.android.myapplication.finanzas.ApplicationInitial

@AndroidEntryPoint
class BoughWalletController: Fragment() {
    @Inject lateinit var boughtSvc: IBoughtPort
    @Inject lateinit var taxSvc:ITaxPort
    @Inject lateinit var creditCardSvc: ICreditCardPort

    val viewModel : WalletViewModel by viewModels{
        ViewModelFactory(
            owner= this,
            viewModelClass= WalletViewModel::class.java,
            build={
                val pair = BoughWalletParams.download(requireArguments())
                WalletViewModel(
                    savedStateHolder = it,
                    codeCreditCard = pair.first,
                    codeBought=pair.second?:0,
                    period=LocalDateTime.now(),
                    creditCardSvc = creditCardSvc,
                    boughtSvc=boughtSvc,
                    creditRateSvc=taxSvc,
                    navController =  findNavController(),
                    prefs = ApplicationInitial.prefs)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Wallet(viewModel = viewModel)
                }
            }
        }
    }


}
