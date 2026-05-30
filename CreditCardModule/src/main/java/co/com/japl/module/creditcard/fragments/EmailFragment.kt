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
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.module.creditcard.controllers.emailcreditcard.form.EmailCreditCardViewModel
import co.com.japl.module.creditcard.views.email.form.EmailBought
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.databinding.FragmentEmailCreditCardBinding
import co.com.japl.ui.factory.ViewModelFactory
import co.com.japl.finances.iports.params.EmailCreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class EmailFragment: Fragment(){

    @Inject lateinit var emailCCSvc : IEmailCreditCardPort
    @Inject lateinit var ccSvc : ICreditCardPort
    @Inject lateinit var llmSvc : ILLMService

    val viewModel : EmailCreditCardViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = EmailCreditCardViewModel::class.java,
            build = {
                val code = EmailCreditCardParams.download(requireArguments())
                EmailCreditCardViewModel(
                    codeEmailCC = code,
                    svc = emailCCSvc,
                    creditCardSvc = ccSvc,
                    navController = findNavController(),
                    llmService = llmSvc,
                    prefs = Prefs(requireContext()),
                    context = requireContext()
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentEmailCreditCardBinding.inflate(inflater,container,false)

        root.cvComposeEmailCc.apply {
            setContent {
                MaterialThemeComposeUI {
                    EmailBought(viewModel = viewModel)
                }
            }
        }
        return root.root.rootView
    }

}