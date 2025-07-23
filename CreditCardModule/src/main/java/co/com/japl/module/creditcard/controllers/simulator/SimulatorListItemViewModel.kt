package co.com.japl.module.creditcard.controllers.simulator

import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.creditcard.R
import java.util.Optional

class SimulatorListItemViewModel constructor(private val itemValue: SimulatorCreditDTO?=null, private val simulatorVariableSvc: ISimulatorCreditVariablePort?=null, private val navController: NavController?=null){

    val showOptions = mutableStateOf(false)
    var item: Optional<SimulatorCreditDTO> = Optional.empty()
      private set

    init{
        itemValue?.let{
            item = Optional.of(it)
        }
    }

    fun gotoAmortization(code:Int){
        navController?.let{
            simulatorVariableSvc?.let{
                it.setSimulation(item.get())
            }
            val path = navController.context.getString(R.string.navigate_form_simulator_amortization,code).toUri()
            it.navigate(path)
        }
    }
}