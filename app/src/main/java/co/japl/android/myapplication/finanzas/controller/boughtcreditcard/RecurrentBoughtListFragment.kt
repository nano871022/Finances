package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.com.japl.module.creditcard.views.bought.RecurrentBoughtList
import co.com.japl.module.creditcard.controllers.bought.lists.RecurrentBoughtViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.Prefs
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.ApplicationInitial
import javax.inject.Inject

@AndroidEntryPoint
class RecurrentBoughtListFragment : Fragment() {

    @Inject lateinit var boughtSvc: IBoughtListPort
    private lateinit var viewModel: RecurrentBoughtViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val codeCreditCard = arguments?.getString(CreditCardQuotesParams.Params.PARAM_CREDIT_CARD_CODE)?.toIntOrNull() ?: 0
        val prefs = ApplicationInitial.prefs

        viewModel = RecurrentBoughtViewModel(boughtSvc, prefs)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    RecurrentBoughtList(
                        codeCreditCard = codeCreditCard,
                        navController = findNavController(),
                        viewModel = viewModel,
                        onEdit = { item ->
                             CreditCardQuotesParams.Companion.ListBought.newInstanceRecurrent(
                                item.id,
                                item.codeCreditCard,
                                findNavController()
                            )
                        },
                        onAlter = { item ->
                             CreditCardQuotesParams.Companion.ListBought.newInstanceRecurrent(
                                0,
                                item.codeCreditCard,
                                findNavController(),
                                oldBoughtId = item.id
                            )
                        }
                    )
                }
            }
        }
    }
}
