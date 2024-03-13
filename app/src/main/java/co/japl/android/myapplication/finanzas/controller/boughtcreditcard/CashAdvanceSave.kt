package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.module.creditcard.views.bought.forms.Advance
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import co.japl.android.myapplication.databinding.CashAdvanceCreditCardBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class CashAdvanceSave: Fragment() {
    @Inject lateinit var creditCardSvc: ICreditCardPort
    @Inject lateinit var boughtSvc: IBoughtPort
    @Inject lateinit var creditRateSvc:ITaxPort

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = CashAdvanceCreditCardBinding.inflate(inflater,container,false)
        val (codeCreditCard,bought) = CashAdvanceParams.download(requireArguments())
        val viewModel = AdvanceViewModel(codeCreditCard,bought?:0,LocalDateTime.now(),boughtSvc,creditRateSvc,creditCardSvc,findNavController(),ApplicationInitial.prefs)
        root.cvComposeCacc.apply {
            setContent {
                MaterialThemeComposeUI {
                    Advance(viewModel = viewModel)
                }
            }
        }
        return root.root.rootView
    }

}