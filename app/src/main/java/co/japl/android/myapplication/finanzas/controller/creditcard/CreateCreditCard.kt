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
import co.japl.android.myapplication.databinding.FragmentCreateCreditCardBinding
import co.com.japl.module.creditcard.controllers.account.CreditCardViewModel
import co.com.japl.module.creditcard.views.account.forms.CreditCard
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.putParams.CreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateCreditCard : Fragment() {
    private var param1: String? = null
    @Inject lateinit var creditCardSvc : ICreditCardPort

    private var _binding:FragmentCreateCreditCardBinding? = null
    private val binding get() = _binding!!

    val viewModel : CreditCardViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardViewModel::class.java,
            build={
                CreditCardViewModel(
                    it,
                    creditCardSvc
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateCreditCardBinding.inflate(inflater)
        binding.cvComposeCcc.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditCard(viewModel,findNavController())
                }
            }
        }

        return binding.root.rootView
    }
}