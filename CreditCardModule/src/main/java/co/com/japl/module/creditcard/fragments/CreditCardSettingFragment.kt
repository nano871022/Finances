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
import co.com.japl.module.creditcard.params.CreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreditCardSettingFragment : Fragment() {

    @Inject
    lateinit var factory: CreditCardSettingViewModel.Factory

    private val viewModel: CreditCardSettingViewModel by viewModels {
        val map = CreditCardSettingParams.download(arguments)
        val codeCreditCard = map[CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]
        val codeCreditCardSetting = map[CreditCardSettingParams.Params.ARG_ID]
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardSettingViewModel::class.java,
            build = {
                factory.create(codeCreditCard, codeCreditCardSetting, findNavController())
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    CreditCardSetting(viewModel = viewModel)
                }
            }
        }
    }
}
