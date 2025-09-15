package co.com.japl.module.credit.controllers.list

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.credit.IAdditional

class AdditionalViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val context: Context,
    private val additionalSvc: IAdditional?,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return AdditionalViewModel(context, handle, additionalSvc) as T
    }
}
