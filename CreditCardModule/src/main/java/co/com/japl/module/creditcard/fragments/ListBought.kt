package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.databinding.FragmentListBoughtBinding
import co.com.japl.ui.Prefs
import co.com.japl.finances.iports.params.CreditCardQuotesParams
import co.japl.android.myapplication.finanzas.view.creditcard.bought.BoughtList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListBought : Fragment() {

    private var _binding:FragmentListBoughtBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBoughtBinding.inflate(inflater, container, false)
        val rootView = binding.root
        val application = requireActivity().application
        val data = ListBoughtViewModel(application,findNavController(),ApplicationInitial.prefs)

        arguments?.let {
            val params = CreditCardQuotesParams.Companion.Historical.download(it)
            data.setParams(params)
        }

        binding.boughtListCompose?.apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    BoughtList(data)
                }
            }
        }

        return rootView
    }
}