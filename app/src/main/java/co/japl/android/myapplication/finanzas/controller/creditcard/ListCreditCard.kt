package co.japl.android.myapplication.finanzas.controller.creditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListCreditCardBinding
import co.com.japl.module.creditcard.views.account.lists.CreditCardList
import co.com.japl.module.creditcard.controllers.account.CreditCardListViewModel
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCard : Fragment()  {

    @Inject lateinit var creditCardSvc:ICreditCardPort
    private lateinit var _binding : FragmentListCreditCardBinding
    private val binding get() = _binding

    val viewModel : CreditCardListViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardListViewModel::class.java,
            build = {
                CreditCardListViewModel(creditCardSvc)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCreditCardBinding.inflate(inflater)
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