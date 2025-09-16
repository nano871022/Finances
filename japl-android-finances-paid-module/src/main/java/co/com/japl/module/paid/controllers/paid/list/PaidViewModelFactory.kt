package co.com.japl.module.paid.controllers.paid.list

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.ui.Prefs

class PaidViewModelFactory(
    private val paidSvc: IPaidPort,
    private val prefs: Prefs,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return PaidViewModel(paidSvc, prefs, handle) as T
    }
}
