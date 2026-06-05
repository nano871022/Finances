package co.com.japl.module.creditcard.fragments

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
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.views.setting.forms.CreditCardSetting
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditCardSettingFragment : Fragment() {

    private val viewModel: CreditCardSettingViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    CreditCardSetting(viewModel = viewModel)
                }
            }
        }
    }
}
