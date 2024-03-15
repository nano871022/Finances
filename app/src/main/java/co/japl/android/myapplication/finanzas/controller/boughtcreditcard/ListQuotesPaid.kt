package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListPeriodsBinding
import co.japl.android.myapplication.finanzas.putParams.PeriodsParams
import co.com.japl.module.creditcard.controllers.paid.BoughtCreditCardViewModel
import co.com.japl.module.creditcard.views.paid.PaidList
import co.japl.android.myapplication.finanzas.ApplicationInitial
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class ListQuotesPaid : Fragment() {
    var creditCardId by Delegates.notNull<Int>()

    @Inject lateinit var port: IBoughtListPort
    private lateinit var _binding : FragmentListPeriodsBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPeriodsBinding.inflate(inflater, container, false)
        val rootView = _binding.root
        arguments?.let{
            creditCardId = PeriodsParams.Companion.Historical.download(it)
        }
        Log.d(javaClass.name,"=== ListQuotesPaid start")
        _binding.composeViewLp.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialThemeComposeUI {
                        PaidList(viewModel = BoughtCreditCardViewModel(port,creditCardId,findNavController(),ApplicationInitial.prefs))

                }
            }
        }
        return rootView
    }
}