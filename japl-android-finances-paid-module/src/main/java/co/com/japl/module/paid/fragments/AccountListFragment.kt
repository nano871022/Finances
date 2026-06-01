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
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.views.accounts.list.AccountList
import co.com.japl.module.paid.controllers.accounts.list.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels

@AndroidEntryPoint
class AccountListFragment : Fragment() {
    private val viewModel: AccountViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    AccountList(viewModel = viewModel)
                }
            }
        }
    }
}
