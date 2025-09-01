package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort
import co.com.japl.module.credit.controllers.extravalue.ExtraValueListViewModel
import co.com.japl.module.credit.views.extravalue.ExtraValueListScreen
import co.japl.android.myapplication.R
import co.japl.android.myapplication.databinding.FragmentExtraValueListBinding
import co.japl.android.myapplication.finanzas.putParams.ExtraValueListParam
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
        val root = FragmentExtraValueListBinding.inflate(inflater)
        root.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                    ExtraValueListScreen(viewModel)
            }
        }
        return root.root
    }
}