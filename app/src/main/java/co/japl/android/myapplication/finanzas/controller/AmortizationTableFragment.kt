package co.japl.android.myapplication.finanzas.controller

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
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort
import co.com.japl.module.creditcard.controllers.amortization.AmortizationViewModel
import co.com.japl.module.creditcard.views.amortization.AmortizationTable
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAmortizationTableBinding
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AmortizationTableFragment : Fragment(){

    @Inject lateinit var amortizationSvc: IAmortizationTablePort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentAmortizationTableBinding.inflate(inflater)
        val code = arguments?.let{ AmortizationTableParams.download(it)["CODE"] as Long }
        val viewModel = AmortizationViewModel( code?.toInt()!!,amortizationSvc,findNavController())
        view.composeViewFat.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    AmortizationTable(viewModel)
                }
            }

        }
        return view.root
    }
}