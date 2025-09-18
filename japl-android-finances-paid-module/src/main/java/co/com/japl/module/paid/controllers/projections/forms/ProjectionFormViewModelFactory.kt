package co.com.japl.module.paid.controllers.projections.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort

class ProjectionFormViewModelFactory(
    private val projectionSvc: IProjectionFormPort
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ProjectionFormViewModel(extras.createSavedStateHandle(), projectionSvc) as T
    }
}
