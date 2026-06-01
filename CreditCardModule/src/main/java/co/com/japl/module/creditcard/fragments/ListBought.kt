package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.params.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.view.creditcard.bought.BoughtList
import dagger.hilt.android.AndroidEntryPoint
import co.japl.android.myapplication.finanzas.ApplicationInitial

@AndroidEntryPoint
class ListBought : Fragment() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val data = ListBoughtViewModel(application,findNavController(),ApplicationInitial.prefs)

        arguments?.let {
            val params = CreditCardQuotesParams.Companion.Historical.download(it)
            data.setParams(params)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    BoughtList(data)
                }
            }
        }
    }
}
