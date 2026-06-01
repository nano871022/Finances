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
import co.com.japl.module.credit.controllers.forms.AdditionalFormViewModel
import co.com.japl.module.credit.views.forms.AdditionalForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.credit.params.AdditionalCreditParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalCreditFragment : Fragment() {

    @Inject
    lateinit var factory: AdditionalFormViewModel.Factory

    private val viewModel: AdditionalFormViewModel by viewModels {
        val additional = arguments?.let { AdditionalCreditParams.download(it) }
        val idCredit = additional?.first ?: 0
        val codeCredit = additional?.second ?: 0
        ViewModelFactory(
            owner = this,
            viewModelClass = AdditionalFormViewModel::class.java,
            build = {
                factory.create(requireContext().applicationContext, it, idCredit, codeCredit, findNavController())
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
                    AdditionalForm(viewModel)
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
