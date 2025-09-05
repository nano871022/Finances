package co.japl.android.myapplication.finanzas.controller.creditfix

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
import co.com.japl.module.credit.controllers.forms.AdditionalFormViewModel
import co.com.japl.module.credit.views.forms.AdditionalForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAdditionalCreditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdditionalCreditFragment : Fragment(){

    val viewModel:AdditionalFormViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentAdditionalCreditBinding.inflate(inflater)
        root.composeViewFacf.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed )
            setContent {
                MaterialThemeComposeUI {
                    AdditionalForm(viewModel,findNavController())
                }
            }
        }

        return root.root
    }
}