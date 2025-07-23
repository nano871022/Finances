package co.japl.android.myapplication.finanzas.controller.simulators.list

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel
import co.com.japl.module.credit.controllers.simulator.SimulatorListItemViewModel as SimulatorListItemViewModelCredit
import kotlinx.coroutines.launch


class ListViewModel  constructor(private val simulatorVariableSvc: ISimulatorCreditVariablePort?=null,private val simulatorFixSvc: ISimulatorCreditFixPort?=null, private val navController: NavController?=null):ViewModel() {
    val progres = mutableStateOf(false)
    val list = mutableListOf<SimulatorCreditDTO>()

    init{
        execute()
    }

    fun createViewModelQuoteVariable(dto: SimulatorCreditDTO): SimulatorListItemViewModel{
        return SimulatorListItemViewModel(
            dto,
            simulatorVariableSvc,
            navController
        )
    }

    fun createViewModelQuoteFix(dto: SimulatorCreditDTO): SimulatorListItemViewModelCredit{
        return SimulatorListItemViewModelCredit(
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
        simulatorFixSvc?.let{
            list.addAll(it.getList())
        }
        simulatorVariableSvc?.let{
            list.addAll(it.getList())
        }
    }
}
