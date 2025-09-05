package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.module.credit.controllers.extravalue.ExtraValueListViewModel
import co.com.japl.module.credit.views.extravalue.ExtraValueListScreen
import co.japl.android.myapplication.databinding.FragmentExtraValueListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExtraValueListController : Fragment() {

    val viewModel:ExtraValueListViewModel  by viewModels ()

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