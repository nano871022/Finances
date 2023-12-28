package co.japl.android.myapplication.finanzas.controller.creditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.common.ICreditCardPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListCreditCardBinding
import co.japl.android.myapplication.finanzas.view.creditcard.list.CreditCardList
import co.japl.android.myapplication.finanzas.view.creditcard.list.CreditCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCard : Fragment()  {

    @Inject lateinit var creditCardSvc:ICreditCardPort
    private lateinit var _binding : FragmentListCreditCardBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCreditCardBinding.inflate(inflater)
        val viewModel = CreditCardViewModel(creditCardSvc,findNavController())
        binding?.listComposeLcc?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditCardList(creditCardViewModel = viewModel)
                }
            }
        }
        return binding.root
    }
}