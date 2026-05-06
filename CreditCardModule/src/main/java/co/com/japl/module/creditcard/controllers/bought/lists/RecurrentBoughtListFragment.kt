package co.com.japl.module.creditcard.controllers.bought.lists

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

@AndroidEntryPoint
class RecurrentBoughtListFragment : Fragment() {

    private lateinit var viewModel: RecurrentBoughtViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val codeCreditCard = arguments?.getString(CreditCardQuotesParams.Params.PARAM_CREDIT_CARD_CODE)?.toInt() ?: 0
        val entryPoint = EntryPoints.get(requireContext().applicationContext, EntryPoint::class.java)
        val boughtSvc = entryPoint.getBoughtCreditCardSvc()
        val creditCardSvc = entryPoint.getCreditCardSvc()
        val prefs = Prefs(requireContext())

        viewModel = RecurrentBoughtViewModel(boughtSvc, prefs)

        val creditCard = creditCardSvc.getCreditCard(codeCreditCard)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    RecurrentBoughtList(
                        codeCreditCard = codeCreditCard,
                        navController = findNavController(),
                        viewModel = viewModel,
                        onEdit = { item ->
                             CreditCardQuotesParams.Companion.ListBought.newInstanceQuote(
                                item.id,
                                item.codeCreditCard,
                                findNavController()
                            )
                        },
                        onAlter = { item ->
                             CreditCardQuotesParams.Companion.ListBought.newInstanceQuote(
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
