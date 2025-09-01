package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.module.creditcard.views.bought.forms.Advance
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.CashAdvanceCreditCardBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class CashAdvanceSave: Fragment() {
    @Inject lateinit var creditCardSvc: ICreditCardPort
    @Inject lateinit var boughtSvc: IBoughtPort
    @Inject lateinit var creditRateSvc:ITaxPort

    val viewModel : AdvanceViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = AdvanceViewModel::class.java,
            build = {
                val (codeCreditCard, bought) = CashAdvanceParams.download(requireArguments())
                AdvanceViewModel(
                    savedStateHandle = it,
                    codeCreditCard = codeCreditCard,
                    codeBought = bought ?: 0,
                    period = LocalDateTime.now(),
                    boughtSvc,
                    creditRateSvc,
                    creditCardSvc,
                    navController = findNavController(),
                    prefs = ApplicationInitial.prefs
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = CashAdvanceCreditCardBinding.inflate(inflater,container,false)

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