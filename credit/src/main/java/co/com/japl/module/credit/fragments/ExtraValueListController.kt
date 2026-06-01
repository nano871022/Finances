package co.com.japl.module.credit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort
import co.com.japl.module.credit.controllers.extravalue.ExtraValueListViewModel
import co.com.japl.module.credit.views.extravalue.ExtraValueListScreen
import co.com.japl.module.credit.params.ExtraValueListParam
import co.com.japl.ui.factory.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ExtraValueListController : Fragment() {

    @Inject lateinit var svc: IExtraValueAmortizationCreditPort

    val viewModel:ExtraValueListViewModel  by viewModels {
        val params = arguments?.let { ExtraValueListParam.download(it) }
        val creditId = params?.first ?: 0
        val kindOf = params?.second
        ViewModelFactory(
            owner = this,
            viewModelClass = ExtraValueListViewModel::class.java,
            build = { state ->
                ExtraValueListViewModel(creditId,svc)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                co.com.japl.ui.theme.MaterialThemeComposeUI {
                    ExtraValueListScreen(viewModel)
                }
            }
        }
    }
}