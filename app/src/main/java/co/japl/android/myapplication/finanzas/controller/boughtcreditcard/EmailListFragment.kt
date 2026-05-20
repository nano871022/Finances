package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import androidx.fragment.app.Fragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.module.creditcard.controllers.emailcreditcard.form.EmailCreditCardViewModel
import co.com.japl.module.creditcard.controllers.emailcreditcard.list.EmailListCreditCardViewModel
import co.com.japl.module.creditcard.views.email.form.EmailBought
import co.com.japl.module.creditcard.views.email.list.EmailList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentEmailCreditCardBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.putParams.EmailCreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class EmailListFragment : Fragment(){

    @Inject lateinit var emailCCSvc: IEmailCreditCardPort

    val viewModel : EmailListCreditCardViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = EmailListCreditCardViewModel::class.java,
            build = {
                EmailListCreditCardViewModel(
                    svc = emailCCSvc,
                    navController = findNavController(),
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
                    EmailList(viewModel = viewModel)
                }
            }
        }
        return root.root.rootView
    }
}