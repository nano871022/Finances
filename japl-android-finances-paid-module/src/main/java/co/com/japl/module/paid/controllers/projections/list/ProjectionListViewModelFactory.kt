package co.com.japl.module.paid.controllers.projections.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort

class ProjectionListViewModelFactory(
    private val context: Context,
    private val projectionListPort: IProjectionListPort
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectionListViewModel(context, projectionListPort) as T
    }
}
