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
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.controllers.emailcreditcard.form.EmailCreditCardViewModel
import co.com.japl.module.creditcard.views.email.forms.EmailForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSmsCreditCardBinding
import co.japl.android.myapplication.putParams.EmailCreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmailFragment : Fragment() {
    @Inject lateinit var creditCardSvc: ICreditCardPort
    @Inject lateinit var emailCreditCardSvc: IEmailCreditCardPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentSmsCreditCardBinding.inflate(inflater, container, false)
        val code = arguments?.let { EmailCreditCardParams.download(it) }
        val viewModel: EmailCreditCardViewModel by viewModels {
            object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return EmailCreditCardViewModel(
                        codeEmailCC = code,
                        svc = emailCreditCardSvc,
                        creditCardSvc = creditCardSvc,
                        navController = findNavController()
                    ) as T
                }
            }
        }
        view.cvComposeLscc?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    EmailForm(viewModel)
                }
            }
        }
        return view.root.rootView
    }
}
