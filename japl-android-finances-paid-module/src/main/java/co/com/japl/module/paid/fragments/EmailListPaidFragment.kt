package co.com.japl.module.paid.fragments

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
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.module.paid.controllers.emailpaid.list.EmailListPaidViewModel
import co.com.japl.module.paid.views.email.list.EmailListPaid
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.factory.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmailListPaidFragment: Fragment(){

    @Inject lateinit var emailPaidSvc : IEmailPaidPort

    val viewModel : EmailListPaidViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = EmailListPaidViewModel::class.java,
            build = {
                EmailListPaidViewModel(
                    svc = emailPaidSvc,
                    navController = findNavController()
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
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    EmailListPaid(viewModel = viewModel)
                }
            }
        }
    }

}
