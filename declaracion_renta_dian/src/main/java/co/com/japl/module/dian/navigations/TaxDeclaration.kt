package co.com.japl.module.dian.navigations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.module.dian.views.TaxDeclaration
import co.com.japl.module.dian.controllers.TaxDeclarationViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaxDeclaration : Fragment() {

    private val viewModel: TaxDeclarationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    TaxDeclaration(viewModel = viewModel)
                }
            }
        }
    }
}
