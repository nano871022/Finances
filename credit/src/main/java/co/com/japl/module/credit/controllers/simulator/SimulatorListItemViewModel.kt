package co.com.japl.module.credit.controllers.simulator

import android.util.Log
import androidx.core.net.toUri
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.module.credit.R
import java.util.Optional

class SimulatorListItemViewModel constructor(private val dto: SimulatorCreditDTO?=null, private val simulatorCreditSvc: ISimulatorCreditFixPort?=null, private val navController: NavController?=null){

    var item: Optional<SimulatorCreditDTO> = Optional.empty()
        private set

    init{
        dto?.let{
            item = Optional.of(it)
        }
    }

    fun gotoAmortization(dto:SimulatorCreditDTO){
        navController?.let{
            simulatorCreditSvc?.let {
                it.setSimulation(item.get())
            }
            val path = it.context.getString(R.string.navigate_form_simulator_amortization_fix,dto.code).toUri()
            it.navigate(path)
        }
    }
}