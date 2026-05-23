package co.com.japl.module.declaracion_renta_dian.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.module.declaracion_renta_dian.view.TaxDeclarationScreen
import co.com.japl.module.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaxDeclarationFragment : Fragment() {

    private val viewModel: TaxDeclarationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TaxDeclarationScreen(viewModel = viewModel)
            }
        }
    }
}
