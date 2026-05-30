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
import co.com.japl.module.credit.controllers.forms.CreditFormViewModel
import co.com.japl.module.credit.views.forms.CreditForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreditFixFragment : Fragment() {

    @Inject
    lateinit var factory: CreditFormViewModel.Factory

    private val viewModel: CreditFormViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditFormViewModel::class.java,
            build = {
                factory.create(it, 0, context, findNavController())
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    CreditForm(viewModel)
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
