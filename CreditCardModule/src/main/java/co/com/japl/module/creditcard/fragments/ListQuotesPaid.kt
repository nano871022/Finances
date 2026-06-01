package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.params.PeriodsParams
import co.com.japl.module.creditcard.controllers.paid.BoughtCreditCardViewModel
import co.com.japl.module.creditcard.views.paid.PaidList
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates
import co.com.japl.ui.Prefs

@AndroidEntryPoint
class ListQuotesPaid : Fragment() {
    var creditCardId by Delegates.notNull<Int>()

    @Inject lateinit var port: IBoughtListPort

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let{
            creditCardId = PeriodsParams.Companion.Historical.download(it)
        }
        Log.d(javaClass.name,"=== ListQuotesPaid start")
        return ComposeView(requireContext()).apply {
                setContent {
                    MaterialThemeComposeUI {
                        PaidList(viewModel = BoughtCreditCardViewModel(port,creditCardId,findNavController(),Prefs(requireContext().applicationContext)))

                }
            }
        }
    }
}
