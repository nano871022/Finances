package co.com.japl.module.paid.controllers.projections.forms

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort

class ProjectionFormViewModelFactory(
    private val context: Context,
    private val projectionSvc: IProjectionFormPort
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return ProjectionFormViewModel(context, savedStateHandle, projectionSvc) as T
    }
}
