package co.com.japl.module.paid.controllers.projections.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.com.japl.module.paid.navigations.Projections
import co.com.japl.ui.utils.initialFieldState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.util.logging.Handler
import javax.inject.Inject

@HiltViewModel(assistedFactory = ProjectionsViewModel.Factory::class)
class ProjectionsViewModel @AssistedInject constructor(@Assisted private val savedStateHandler: SavedStateHandle,private val projectionSvc: IProjectionsPort?, @Assisted private val navController: NavController?) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandler: SavedStateHandle, navController: NavController?): ProjectionsViewModel
    }

    val loadingStatus = mutableStateOf(false)
    val projectionsList = mutableStateListOf<ProjectionRecap>()

    val totalCount = initialFieldState(
        savedStateHandler!!,
        "FORM_TOTAL_COUNT",
        initialValue = 0
    )
    val totalSaved = initialFieldState(
        savedStateHandler!!,
        "FORM_TOTAL_SAVED",
        initialValue = BigDecimal.ZERO
    )

    init{
        viewModelScope.launch {
            main()
        }
    }

    fun goToList(){
        navController?.let {
            Projections.listNavigate(it)
        }
    }

    fun goToCreate(){
        navController?.let {
            Projections.formNavigate(it)
        }
    }

    fun main() = runBlocking {
        loadingStatus.value = true
        execute()
    }

    suspend fun execute(){
        projectionSvc?.let{
            it.getProjectionRecap().let{
                it.first.let(totalCount::onValueChange)
                it.second.let(totalSaved::onValueChange)
                it.third.let{
                    projectionsList.clear()
                    projectionsList.addAll(it)
                }
            }
        }
        loadingStatus.value = false
    }

}