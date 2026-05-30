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
import co.com.japl.module.credit.controllers.amortization.AmortizationViewModel
import co.com.japl.module.credit.views.amortization.AmortizationScreen
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.finances.iports.params.AmortizationTableParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AmortizationTableFragment : Fragment() {

    @Inject
    lateinit var factory: AmortizationViewModel.Factory

    private val viewModel: AmortizationViewModel by viewModels {
        val code = arguments?.let {
            val code = AmortizationTableParams.download(it)["CODE"] as Long
            val creditCode = AmortizationTableParams.download(it)["CREDIT_CODE"] as Long
            if (code == 0L) creditCode else code
        } ?: 0L
        val lastDate = arguments?.let { AmortizationTableParams.download(it)["LAST_DATE"] as LocalDate } ?: LocalDate.now()
        ViewModelFactory(
            owner = this,
            viewModelClass = AmortizationViewModel::class.java,
            build = {
                factory.create(requireContext(), it, code.toInt(), lastDate)
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
                    AmortizationScreen(viewModel)
                }
            }
        }
    }
}

private class ViewModelFactory<T : androidx.lifecycle.ViewModel>(
    owner: androidx.savedstate.SavedStateRegistryOwner,
    private val viewModelClass: Class<T>,
    private val build: (androidx.lifecycle.SavedStateHandle) -> T
) : androidx.lifecycle.AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : androidx.lifecycle.ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: androidx.lifecycle.SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return build(handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
