package co.com.japl.module.paid.controllers.projections.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort

class ProjectionListViewModelFactory(
    private val projectionListPort: IProjectionListPort
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ProjectionListViewModel(projectionListPort) as T
    }
}
