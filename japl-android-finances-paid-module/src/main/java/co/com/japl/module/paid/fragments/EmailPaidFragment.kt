package co.japl.android.myapplication.finanzas.controller.paids

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
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.module.paid.controllers.emailpaid.form.EmailPaidViewModel
import co.com.japl.module.paid.views.email.form.EmailPaid
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.databinding.FragmentEmailCreditCardBinding
import co.com.japl.ui.factory.ViewModelFactory
import co.com.japl.module.paid.params.EmailPaidsParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmailPaidFragment: Fragment(){

    @Inject lateinit var emailPaidSvc : IEmailPaidPort
    @Inject lateinit var accountSvc : IAccountPort
    @Inject lateinit var llmSvc : ILLMService

    val viewModel : EmailPaidViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = EmailPaidViewModel::class.java,
            build = {
                val code = EmailPaidsParams.download(requireArguments())
                EmailPaidViewModel(
                    codeEmailPaid = code,
                    svc = emailPaidSvc,
                    accountSvc = accountSvc,
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
    ): View {
        val root = FragmentEmailCreditCardBinding.inflate(inflater,container,false)

        root.cvComposeEmailCc.apply {
            setContent {
                MaterialThemeComposeUI {
                    EmailPaid(viewModel = viewModel)
                }
            }
        }
        return root.root
    }

}
