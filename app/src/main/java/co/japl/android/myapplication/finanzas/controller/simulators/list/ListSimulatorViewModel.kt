package co.japl.android.myapplication.finanzas.controller.simulators.list

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.credit.controllers.simulator.SimulatorListItemViewModel as SimulatorListItemViewModelCredit
import co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel as SimulatorListItemViewModelCreditCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListSimulatorViewModel @Inject constructor(
    private val simulatorVariableSvc: ISimulatorCreditVariablePort?,
    private val simulatorFixSvc: ISimulatorCreditFixPort?
) : ViewModel() {
    val progress = mutableStateOf(false)
    val list = mutableStateListOf<SimulatorCreditDTO>()

    fun load() = viewModelScope.launch {
        progress.value = true
        list.clear()
        val fixList = withContext(Dispatchers.IO) {
            simulatorFixSvc?.getList() ?: emptyList()
        }
        val variableList = withContext(Dispatchers.IO) {
            simulatorVariableSvc?.getList() ?: emptyList()
        }
        list.addAll(fixList)
        list.addAll(variableList)
        progress.value = false
    }

    fun createViewModelQuoteFix(context: Context, dto: SimulatorCreditDTO, navController: NavController?): SimulatorListItemViewModelCredit {
        return SimulatorListItemViewModelCredit(
            context,
            dto,
            simulatorFixSvc,
            navController
        )
    }

    fun createViewModelQuoteVariable(context: Context, dto: SimulatorCreditDTO, navController: NavController?): SimulatorListItemViewModelCreditCard {
        return SimulatorListItemViewModelCreditCard(
            context,
            dto,
            simulatorVariableSvc,
            navController
        )
    }
}
