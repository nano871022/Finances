package co.com.japl.module.credit.controllers.extravalue

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort

class ExtraValueListViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val extraValueAmortizationCreditSvc: IExtraValueAmortizationCreditPort,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return ExtraValueListViewModel(extraValueAmortizationCreditSvc, handle) as T
    }
}
