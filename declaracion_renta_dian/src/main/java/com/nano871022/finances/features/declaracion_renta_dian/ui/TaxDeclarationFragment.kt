package com.nano871022.finances.features.declaracion_renta_dian.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nano871022.finances.features.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
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
