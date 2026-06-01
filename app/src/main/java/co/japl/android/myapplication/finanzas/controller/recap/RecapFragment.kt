package co.japl.android.myapplication.finanzas.controller.recap

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.controller.recap.views.Recap
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import co.japl.android.myapplication.finanzas.ApplicationInitial

@AndroidEntryPoint
class RecapFragment @Inject constructor() : Fragment() {
    @Inject lateinit var recapSvc:IRecapPort
    @Inject lateinit var boughtCreditCardSvc : IBought

    private val recapViewModel by lazy {RecapViewModel(recapSvc,boughtCreditCardSvc,ApplicationInitial.prefs,findNavController())}

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Recap(recapViewModel)
                }
            }
        }
    }

}