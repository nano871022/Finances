package co.japl.android.myapplication.finanzas.controller.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.databinding.FragmentLlmConnectionBinding
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.finanzas.view.setting.LLMConnectionForm
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LLMConnectionSetting : Fragment() {

    @Inject lateinit var llmSvc: ILLMService

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLlmConnectionBinding.inflate(inflater)
        val viewModel = LLMConnectionViewModel(prefs = ApplicationInitial.prefs, context = requireContext(), llmService = llmSvc)
        binding.composeViewLlmcnt.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    LLMConnectionForm(viewModel = viewModel)
                }
            }
        }
        return binding.root.rootView
    }
}
