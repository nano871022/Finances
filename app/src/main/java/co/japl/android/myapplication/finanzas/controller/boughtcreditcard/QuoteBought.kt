package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.creditcard.controllers.bought.forms.QuoteViewModel
import co.com.japl.module.creditcard.views.bought.forms.Quote
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.BuysCreditCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteBought : Fragment(){

    val viewModel:QuoteViewModel by viewModels{
        ViewModelFactory(
            owner=this,
            viewModelClass=QuoteViewModel::class.java,
            build = {
                QuoteViewModel(
                    boughtSvc= saveSvc,
                    creditRateSvc= taxSvc,
                    creditCardSvc= creditCardSvc,
                    savedStateHandle = it,
                    tagSvc= tagSvc,
                    creditCardSettingSvc= settingCCSvc,
                    buyCreditCardSettingSvc= buyCCSSvc,
                    navController=findNavController(),
                    prefs=ApplicationInitial.prefs
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = BuysCreditCardBinding.inflate(inflater, container, false)

        root.cvComposableBcc.apply {
            setContent {
                MaterialThemeComposeUI {
                    Quote(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return root.root.rootView

    }
}
