package co.com.japl.module.credit.fragments

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
// Removed: import co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import co.com.japl.module.credit.controllers.simulator.SimulatorListItemViewModel as SimulatorListItemViewModelCredit
import kotlinx.coroutines.launch


class ListViewModel(@ApplicationContext val context: Context, private val simulatorVariableSvc: ISimulatorCreditVariablePort?=null, private val simulatorFixSvc: ISimulatorCreditFixPort?=null, private val navController: NavController?=null):ViewModel() {
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

    fun createViewModelQuoteFix(dto: SimulatorCreditDTO): SimulatorListItemViewModelCredit{
        return SimulatorListItemViewModelCredit(
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
        simulatorFixSvc?.let{
            list.addAll(it.getList())
        }
        simulatorVariableSvc?.let{
            list.addAll(it.getList())
        }
    }
}
