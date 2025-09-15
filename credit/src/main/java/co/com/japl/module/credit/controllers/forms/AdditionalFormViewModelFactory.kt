package co.com.japl.module.credit.controllers.forms

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort

class AdditionalFormViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val context: Context,
    private val additionalSvc: IAdditionalFormPort,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return AdditionalFormViewModel(context, handle, additionalSvc) as T
    }
}
