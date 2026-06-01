package co.com.japl.module.credit.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.credit.controllers.recap.RecapViewModel
import co.com.japl.module.credit.views.recap.Recap
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditListFragment : Fragment() {

    private val viewModel: RecapViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Recap(viewModel)
                }
            }
        }
    }
}
