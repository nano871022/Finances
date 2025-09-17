package co.com.japl.module.paid.controllers.projections.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort

class ProjectionsViewModelFactory(
    private val projectionsPort: IProjectionsPort
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return ProjectionsViewModel(savedStateHandle, projectionsPort) as T
    }
}
