package co.japl.android.myapplication.finanzas.controller.taxes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.module.creditcard.controllers.creditrate.lists.CreditRateListViewModel
import co.com.japl.module.creditcard.views.creditrate.lists.CreditRateList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListTaxCreditCardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListTaxCreditCard : Fragment() {

    @Inject lateinit var creditRateSvc: ITaxPort
    @Inject lateinit var creditCardSvc2: ICreditCardPort

    lateinit var _bind : FragmentListTaxCreditCardBinding
    val bind get() = _bind

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentListTaxCreditCardBinding.inflate(inflater)
        val viewModel = CreditRateListViewModel(requireContext(),creditCardSvc2,creditRateSvc,findNavController())
        bind.cvComposableTcc.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditRateList(viewModel = viewModel)
                }
            }
        }
        return bind.root.rootView
    }

}