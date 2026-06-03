package co.com.japl.module.credit.controllers.simulator

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimulatorListViewModel(@ApplicationContext val context: Context, private val simulatorVariableSvc: ISimulatorCreditVariablePort?=null, private val simulatorFixSvc: ISimulatorCreditFixPort?=null, private val navController: NavController?=null):
    ViewModel() {
    val progres = mutableStateOf(false)
    val list = mutableListOf<SimulatorCreditDTO>()

    init{
        execute()
    }


    /*
    fun createViewModelQuoteVariable(dto: SimulatorCreditDTO): Any?{
        return null
    }
    */

    fun createViewModelQuoteFix(dto: SimulatorCreditDTO): SimulatorListItemViewModel {
        return SimulatorListItemViewModel(
            context,
            dto,
            simulatorFixSvc,
            navController
        )
    }

    fun execute()= viewModelScope.launch {
        progres.value = true
        running()
        progres.value = false
    }

    suspend fun running(){
        withContext(Dispatchers.IO) {
            simulatorFixSvc?.getList()
        }?.let{
            list.addAll(it)
        }
        withContext(Dispatchers.IO) {
            simulatorVariableSvc?.getList()
        }?.let{
            list.addAll(it)
        }
    }
}