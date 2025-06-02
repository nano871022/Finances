package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.module.credit.controllers.forms.CreditFormViewModel
import co.com.japl.module.credit.views.forms.CreditForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentCreditFixBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreditFixFragment : Fragment(){
    @Inject lateinit var creditSvc:ICreditFormPort

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentCreditFixBinding.inflate(inflater, container, false)
        val viewModel = CreditFormViewModel(0, creditSvc, context, findNavController())
        root.cvComponentCf.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditForm(viewModel)
                }
            }
            return root.root
        }
    }
}