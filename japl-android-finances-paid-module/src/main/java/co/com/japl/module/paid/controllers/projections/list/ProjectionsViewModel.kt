package co.com.japl.module.paid.controllers.projections.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.com.japl.module.paid.navigations.Projections
import co.com.japl.ui.utils.initialFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ProjectionsViewModel @Inject constructor(private val projectionSvc: IProjectionsPort?=null, private val navController: NavController?=null) : ViewModel() {

    val loadingStatus = mutableStateOf(false)
    val projectionsList = mutableStateListOf<ProjectionRecap>()

    val totalCount = initialFieldState(
        initialValue = 0
    )
    val totalSaved = initialFieldState(
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