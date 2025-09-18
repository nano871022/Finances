package co.com.japl.module.paid.controllers.projections.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.com.japl.module.paid.navigations.Projections
import co.com.japl.ui.utils.initialFieldState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import javax.inject.Inject

class ProjectionsViewModel @Inject constructor(
    private val savedStateHandler: SavedStateHandle,
    private val projectionSvc: IProjectionsPort
) : ViewModel() {

    val loadingStatus = mutableStateOf(false)
    val projectionsList = mutableStateListOf<ProjectionRecap>()

    val totalCount = initialFieldState(
        savedStateHandler,
        "FORM_TOTAL_COUNT",
        initialValue = 0
    )
    val totalSaved = initialFieldState(
        savedStateHandler,
        "FORM_TOTAL_SAVED",
        initialValue = BigDecimal.ZERO
    )

    init {
        viewModelScope.launch {
            main()
        }
    }

    fun goToList(navController: NavController) {
        Projections.listNavigate(navController)
    }

    fun goToCreate(navController: NavController) {
        Projections.formNavigate(navController)
    }

    fun main() = runBlocking {
        loadingStatus.value = true
        execute()
    }

    suspend fun execute() {
        projectionSvc.let {
            it.getProjectionRecap().let {
                it.first.let(totalCount::onValueChange)
                it.second.let(totalSaved::onValueChange)
                it.third.let {
                    projectionsList.clear()
                    projectionsList.addAll(it)
                }
            }
        }
        loadingStatus.value = false
    }

    companion object {
        fun create(extras: CreationExtras, projectionSvc: IProjectionsPort): ProjectionsViewModel {
            val savedStateHandle = extras.createSavedStateHandle()
            return ProjectionsViewModel(savedStateHandle, projectionSvc)
        }
    }
}
