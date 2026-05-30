package co.com.japl.module.credit.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationViewModel
import co.com.japl.module.credit.views.creditamortization.CreditAmortizationScreen
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.finances.iports.params.AmortizationCreditParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AmortizationCreditFragment : Fragment() {

    @Inject
    lateinit var factory: CreditAmortizationViewModel.Factory

    private val viewModel: CreditAmortizationViewModel by viewModels {
        val map = arguments?.let { AmortizationCreditParams.download(it) }
        val creditCode = ((map?.get("CREDIT_CODE") as Long?) ?: 0L).toInt()
        val lastDate = (map?.get("LAST_DATE") as LocalDate?) ?: LocalDate.now()
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditAmortizationViewModel::class.java,
            build = {
                factory.create(creditCode, lastDate, findNavController())
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    CreditAmortizationScreen(viewModel)
                }
            }
        }
    }
}

private class ViewModelFactory<T : androidx.lifecycle.ViewModel>(
    owner: androidx.savedstate.SavedStateRegistryOwner,
    private val viewModelClass: Class<T>,
    private val build: () -> T
) : androidx.lifecycle.AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : androidx.lifecycle.ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: androidx.lifecycle.SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return build() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
