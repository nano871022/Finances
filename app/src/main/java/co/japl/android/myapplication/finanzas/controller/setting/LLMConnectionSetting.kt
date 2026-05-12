package co.japl.android.myapplication.finanzas.controller.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentLlmConnectionBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.view.setting.LLMConnectionForm

class LLMConnectionSetting : Fragment() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLlmConnectionBinding.inflate(inflater)
        val viewModel = LLMConnectionViewModel(prefs = ApplicationInitial.prefs, context = requireContext())
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
