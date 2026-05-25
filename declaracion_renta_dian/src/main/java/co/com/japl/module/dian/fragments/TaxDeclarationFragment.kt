package co.com.japl.module.dian.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.module.dian.controllers.TaxDeclarationViewModel
import dagger.hilt.android.AndroidEntryPoint
import co.com.japl.module.dian.views.TaxDeclaration
import co.com.japl.ui.theme.MaterialThemeComposeUI

@AndroidEntryPoint
class TaxDeclarationFragment : Fragment() {
    private val vm: TaxDeclarationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply{
            setContent {
                MaterialThemeComposeUI {
                    TaxDeclaration(vm)
                }
            }
        }
    }
}