package co.japl.android.myapplication.finanzas.controller.simulators.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.credit.controllers.simulator.SimulatorListItemViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import co.com.japl.module.credit.controllers.simulator.SimulatorListItemViewModel as SimulatorListItemViewModelCredit

class ListViewModel  @Inject constructor(private val simulatorVariableSvc: ISimulatorCreditVariablePort,private val simulatorFixSvc: ISimulatorCreditFixPort):ViewModel() {
    val progres = mutableStateOf(false)
    val list = mutableListOf<SimulatorCreditDTO>()

    init{
        execute()
    }

    fun createViewModelQuoteVariable(dto: SimulatorCreditDTO): co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel {
        return co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel(
            dto,
            simulatorVariableSvc
        )
    }

    fun createViewModelQuoteFix(dto: SimulatorCreditDTO, navController: NavController): SimulatorListItemViewModelCredit{
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
        simulatorFixSvc.let{
            list.addAll(it.getList())
        }
        simulatorVariableSvc.let{
            list.addAll(it.getList())
        }
    }
}
