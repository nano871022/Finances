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
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.views.accounts.list.AccountList
import co.com.japl.module.paid.controllers.accounts.list.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountListFragment : Fragment() {
    @Inject lateinit var service:IAccountPort
    @Inject lateinit var inputSvc:IInputPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = AccountViewModel(service,inputSvc, findNavController())
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    AccountList(viewModel = viewModel)
                }
            }
        }
    }
}
