package co.com.japl.module.creditcard.fragments

import androidx.fragment.app.Fragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.controllers.emailcreditcard.list.EmailListCreditCardViewModel
import co.com.japl.module.creditcard.views.email.list.EmailList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.factory.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    EmailList(viewModel = viewModel)
                }
            }
        }
    }
}
