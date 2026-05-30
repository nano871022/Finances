package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingListViewModel
import co.com.japl.module.creditcard.views.setting.lists.CreditCardSettingList
import co.com.japl.finances.iports.params.ListCreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCardSetting : Fragment() {

    @Inject
    lateinit var factory: CreditCardSettingListViewModel.Factory

    private val viewModel: CreditCardSettingListViewModel by viewModels {
        val map = ListCreditCardSettingParams.download(arguments)
        val codeCreditCard = if (map.containsKey(ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD)) {
            map[ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]!!
        } else {
            0
        }
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardSettingListViewModel::class.java,
            build = {
                factory.create(codeCreditCard, findNavController())
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
                    CreditCardSettingList(viewModel = viewModel)
                }
            }
        }
    }
}
