package co.com.japl.module.credit.controllers.list

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort

class PeriodsViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val periodSvc: IPeriodCreditPort?,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return PeriodsViewModel(handle, periodSvc) as T
    }
}
