package co.japl.android.myapplication.finanzas.controller.creditcard

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentCreditCardSettingBinding
import co.com.japl.module.creditcard.views.setting.forms.CreditCardSetting
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingViewModel
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreditCardSettingFragment : Fragment() {

    @Inject lateinit var creditCardSvc: ICreditCardPort
    @Inject lateinit var creditCardSettingSvc:ICreditCardSettingPort

    private var _binding:FragmentCreditCardSettingBinding? = null
    val binding get() = _binding

    val viewModel : CreditCardSettingViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardSettingViewModel::class.java,
            build = {
                CreditCardSettingViewModel(
                    savedStateHandle = it
                    , creditCardSvc=creditCardSvc
                    , creditCardSettingSvc=creditCardSettingSvc
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreditCardSettingBinding.inflate(inflater)
        binding?.cvComposeCcs?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditCardSetting(viewModel = viewModel)
                }
            }
        }
        return binding?.root?.rootView
    }
}