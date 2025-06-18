package co.com.japl.module.paid.controllers.projections.list
import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.navigations.Projections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectionListViewModel @Inject constructor(
    private val context: Context,
    private val projectionListPort: IProjectionListPort? = null,
    private val navController: NavController? = null
): ViewModel() {
    val snackbarHost = SnackbarHostState()
    val loader = mutableStateOf(false)
    private val _list = mutableStateListOf<ProjectionDTO>()
    val list = _list

    init {
        load()
    }

    fun goToCreate() {
        navController?.let {
            Projections.formNavigate(it)
        }
    }

    fun edit(id: Int) {
        navController?.let{
            Projections.formNavigate(id,it)
        }
    }

    fun delete(id: Int) {
        projectionListPort?.let { svc ->
            svc.delete(id).let {
                viewModelScope.launch {
                    if (it) {
                        snackbarHost.showSnackbar(
                            message = context.getString(R.string.record_was_remove_success),
                            actionLabel = context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        ).also {
                            load()
                        }
                    } else {
                        snackbarHost.showSnackbar(
                            message = context.getString(R.string.record_was_remove_error),
                            actionLabel = context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    private fun load() = viewModelScope.launch {
        loader.value = true
        projectionListPort?.let {
            it.getProjections().let {
                _list.clear()
                _list.addAll(it)
            }
        }.also {
            loader.value = false
        }
    }
}
