package co.japl.android.myapplication.finanzas.controller.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSettingsAppBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.view.setting.SettingsApp
import javax.inject.Inject

class SettingsApp : Fragment() {
    @Inject lateinit var prefs : Prefs

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ):View?{

        val binding = FragmentSettingsAppBinding.inflate(inflater)
        val viewModel = SettingsAppViewModel(prefs=prefs, context = requireContext())
        binding.cvComposeFsa.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    SettingsApp(viewModel=viewModel)
                }
            }
        }
        return binding.root.rootView
    }

}