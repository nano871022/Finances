package co.com.japl.module.creditcard.fragments

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
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.controllers.account.CreditCardViewModel
import co.com.japl.module.creditcard.views.account.forms.CreditCard
import co.com.japl.finances.iports.params.CreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateCreditCard : Fragment() {

    @Inject
    lateinit var factory: CreditCardViewModel.Factory

    private val viewModel: CreditCardViewModel by viewModels {
        val code = arguments?.let {
            CreditCardParams.download(it).orElse("0")
        } ?: "0"
        ViewModelFactory(
            owner = this,
            viewModelClass = CreditCardViewModel::class.java,
            build = {
                factory.create(code.toInt(), findNavController())
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
                    CreditCard(viewModel)
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
