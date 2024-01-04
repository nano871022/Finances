package co.japl.android.myapplication.finanzas.controller.creditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListCreditCardSettingBinding
import co.com.japl.module.creditcard.views.setting.lists.CreditCardSettingList
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingListViewModel
import co.japl.android.myapplication.putParams.ListCreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCardSetting : Fragment() {
    private var codeCreditCard : Int = 0

    @Inject lateinit var saveSvc: ICreditCardSettingPort

     lateinit var _binding:FragmentListCreditCardSettingBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCreditCardSettingBinding.inflate(inflater)
        val map = ListCreditCardSettingParams.download(arguments)
        if(map.containsKey(ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD)) {
            codeCreditCard = map[ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]!!
        }
        val viewModel = CreditCardSettingListViewModel(codeCreditCard,saveSvc,findNavController())
        binding?.cvComposeLccs?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditCardSettingList(viewModel = viewModel)
                }
            }
        }
        return binding.root.rootView
    }


}