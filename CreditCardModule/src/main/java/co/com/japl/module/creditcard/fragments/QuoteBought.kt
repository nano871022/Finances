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
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.controllers.bought.forms.QuoteViewModel
import co.com.japl.module.creditcard.views.bought.forms.Quote
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.factory.ViewModelFactory
import co.com.japl.module.creditcard.params.CreditCardQuotesParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject
import co.com.japl.ui.Prefs

@AndroidEntryPoint
class QuoteBought : Fragment(){


    @Inject lateinit var taxSvc: ITaxPort
    @Inject lateinit var creditCardSvc:ICreditCardPort
    @Inject lateinit var saveSvc: IBoughtPort
    @Inject lateinit var buyCCSSvc: IBuyCreditCardSettingPort
    @Inject lateinit var settingCCSvc: ICreditCardSettingPort
    @Inject lateinit var tagSvc:ITagPort

    val viewModel:QuoteViewModel by viewModels{
        ViewModelFactory(
            owner=this,
            viewModelClass=QuoteViewModel::class.java,
            build = {
                val (codeCreditCard,_,codeBought) = CreditCardQuotesParams.Companion.CreateQuote.download(requireArguments())
                val oldBoughtId = CreditCardQuotesParams.Companion.CreateQuote.downloadOldBoughtId(requireArguments())
                QuoteViewModel(
                    codeCreditCard=codeCreditCard,
                    codeBought=codeBought,
                    oldBoughtId = oldBoughtId,
                    period= LocalDateTime.now(),
                    boughtSvc= saveSvc,
                    creditRateSvc= taxSvc,
                    creditCardSvc= creditCardSvc,
                    savedStateHandle = it,
                    tagSvc= tagSvc,
                    creditCardSettingSvc= settingCCSvc,
                    buyCreditCardSettingSvc= buyCCSSvc,
                    navController=findNavController(),
                    prefs=Prefs(requireContext().applicationContext)
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Quote(viewModel = viewModel)
                }
            }
        }
    }
}
