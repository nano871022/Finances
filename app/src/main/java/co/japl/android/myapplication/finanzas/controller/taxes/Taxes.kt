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
import co.com.japl.module.creditcard.controllers.creditrate.forms.CreateRateViewModel
import co.com.japl.module.creditcard.views.creditrate.forms.CreditRate
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentTaxesBinding
import co.japl.android.myapplication.putParams.TaxesParams
import co.com.japl.utils.NumbersUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Taxes : Fragment() {

    @Inject lateinit var service:ITaxPort
    @Inject lateinit var creditCardSvc:ICreditCardPort

    private lateinit var _binding : FragmentTaxesBinding
    private var codeCreditCard:Int? = null
    private var codeCreditRate:Int? = null


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaxesBinding.inflate(inflater)
        getParameters()
        val viewModel = CreateRateViewModel(codeCreditCard,codeCreditRate,service,creditCardSvc,findNavController())
        _binding.cvComponentFts.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditRate(viewModel = viewModel)
                }
            }
        }
        return _binding.root.rootView
    }

    private fun getParameters(){
        arguments?.let {
            val params = TaxesParams.download(it)
            codeCreditCard = if(NumbersUtil.isNumber(params.first))
                params.first.toInt()
            else
                null
            codeCreditRate = if(NumbersUtil.isNumber(params.second))
                params.second.toInt()
            else
                null
        }
    }

}