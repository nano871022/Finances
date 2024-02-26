package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.databinding.FragmentListBoughtBinding
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.view.creditcard.bought.BoughtList
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListBought : Fragment() {

    @Inject lateinit var taxSvc:ITaxSvc
    @Inject lateinit var boughtListSvc:IBoughtListPort
    @Inject lateinit var creditCardSvc:ICreditCardPort
    @Inject lateinit var differInstallmentSvc:IDifferQuotesPort

    private var _binding:FragmentListBoughtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBoughtBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val data = ListBoughtViewModel(taxSvc,boughtListSvc,creditCardSvc,differInstallmentSvc,findNavController())

        arguments?.let {
            val params = CreditCardQuotesParams.Companion.Historical.download(it)
            data.setParams(params)
        }

        binding.boughtListCompose?.apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    BoughtList(data)
                }
            }
        }

        return rootView
    }
}