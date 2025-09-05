package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.creditcard.controllers.bought.forms.WalletViewModel
import co.com.japl.module.creditcard.views.bought.forms.Wallet
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.BuyWalletCreditCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoughWalletController: Fragment() {

    val viewModel : WalletViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BuyWalletCreditCardBinding.inflate(inflater,container,false)
        binding.cvWalletBwcc.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Wallet(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return binding.root.rootView
    }


}