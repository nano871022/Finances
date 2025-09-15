package co.com.japl.module.credit.controllers.amortization

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort

class AmortizationViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val amortizationSvc: IAmortizationTablePort,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return AmortizationViewModel(handle, amortizationSvc) as T
    }
}
