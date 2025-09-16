package co.japl.android.myapplication.finanzas.controller.creditcard

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
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListCreditCardSettingBinding
import co.com.japl.module.creditcard.views.setting.lists.CreditCardSettingList
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingListViewModel
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.putParams.ListCreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCardSetting : Fragment() {
    private var codeCreditCard : Int = 0

    @Inject lateinit var saveSvc: ICreditCardSettingPort

     lateinit var _binding:FragmentListCreditCardSettingBinding
    private val binding get() = _binding

    val viewModel : CreditCardSettingListViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardSettingListViewModel::class.java,
            build = {
                CreditCardSettingListViewModel(it,saveSvc)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCreditCardSettingBinding.inflate(inflater)
        binding?.cvComposeLccs?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditCardSettingList(viewModel = viewModel,findNavController())
                }
            }
        }
        return binding.root.rootView
    }


}