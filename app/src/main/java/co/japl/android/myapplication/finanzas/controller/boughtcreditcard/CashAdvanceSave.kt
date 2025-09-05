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
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.module.creditcard.views.bought.forms.Advance
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.CashAdvanceCreditCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CashAdvanceSave: Fragment() {

    val viewModel : AdvanceViewModel by viewModels()

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
                    Advance(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return root.root.rootView
    }

}