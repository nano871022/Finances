package co.com.japl.module.paid.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.params.AccountParams
import co.com.japl.module.paid.views.accounts.form.AccountForm
import co.com.japl.module.paid.controllers.accounts.form.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    @Inject lateinit var accountSvc:IAccountPort
    private var idAccount = 0

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            idAccount = AccountParams.download(it)
        }

        val viewModel = AccountViewModel(idAccount,accountSvc,findNavController())
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    AccountForm(viewModel = viewModel)
                }
            }
        }
    }

}
