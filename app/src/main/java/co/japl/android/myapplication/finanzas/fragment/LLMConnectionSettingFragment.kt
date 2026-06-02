package co.japl.android.myapplication.finanzas.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.controller.recap.RecapViewModel
import co.japl.android.myapplication.finanzas.controller.setting.LLMConnectionViewModel
import co.japl.android.myapplication.finanzas.view.recap.Recap
import co.japl.android.myapplication.finanzas.view.setting.LLMConnectionForm
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LLMConnectionSettingFragment : Fragment() {

    @Inject lateinit var prefs: Prefs
    @Inject lateinit var llmSvc: ILLMService

    private val viewModel by lazy {
        LLMConnectionViewModel(
            prefs=prefs,
            context=requireContext(),
            llmService=llmSvc
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    LLMConnectionForm(viewModel)
                }
            }
        }
    }

}