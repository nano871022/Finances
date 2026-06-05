package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.module.creditcard.controllers.creditrate.lists.CreditRateListViewModel
import co.com.japl.module.creditcard.views.creditrate.lists.CreditRateList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListTaxCreditCardFragment : Fragment() {

    @Inject lateinit var creditRateSvc: ITaxPort
    @Inject lateinit var creditCardSvc2: ICreditCardPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = CreditRateListViewModel(requireContext(),creditCardSvc2,creditRateSvc,findNavController())
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    CreditRateList(viewModel = viewModel)
                }
            }
        }
    }

}
