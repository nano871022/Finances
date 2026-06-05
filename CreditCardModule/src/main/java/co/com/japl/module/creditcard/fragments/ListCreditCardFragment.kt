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
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.views.account.lists.CreditCardList
import co.com.japl.module.creditcard.controllers.account.CreditCardListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCreditCard : Fragment()  {

    private val viewModel: CreditCardListViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            viewModel.setNavController(findNavController())
            setContent {
                MaterialThemeComposeUI {
                    CreditCardList(creditCardViewModel = viewModel)
                }
            }
        }
    }
}
