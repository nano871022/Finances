package co.japl.android.myapplication.finanzas.controller.taxes

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
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.module.creditcard.controllers.creditrate.forms.CreateRateViewModel
import co.com.japl.module.creditcard.views.creditrate.forms.CreditRate
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentTaxesBinding
import co.japl.android.myapplication.putParams.TaxesParams
import co.com.japl.utils.NumbersUtil
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Taxes : Fragment() {

    @Inject lateinit var service:ITaxPort
    @Inject lateinit var creditCardSvc:ICreditCardPort

    private lateinit var _binding : FragmentTaxesBinding
    private var codeCreditCard:Int? = null
    private var codeCreditRate:Int? = null

    val viewModel : CreateRateViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = CreateRateViewModel::class.java,
            build = {
                CreateRateViewModel(it,
                    service,
                    creditCardSvc)
            }

        )
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaxesBinding.inflate(inflater)
        _binding.cvComponentFts.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditRate(viewModel = viewModel,findNavController())
                }
            }
        }
        return _binding.root.rootView
    }
}