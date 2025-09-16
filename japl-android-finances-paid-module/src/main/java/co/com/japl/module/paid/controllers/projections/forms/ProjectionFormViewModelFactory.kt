package co.com.japl.module.paid.controllers.projections.forms

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort

class ProjectionFormViewModelFactory(
    private val context: Context,
    private val projectionSvc: IProjectionFormPort,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return ProjectionFormViewModel(context, handle, projectionSvc) as T
    }
}
