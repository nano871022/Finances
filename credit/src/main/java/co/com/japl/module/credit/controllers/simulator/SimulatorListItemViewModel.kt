package co.com.japl.module.credit.controllers.simulator

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.com.japl.module.credit.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Optional

class SimulatorListItemViewModel(@ApplicationContext val context: Context, private val dto: SimulatorCreditDTO?=null, private val simulatorCreditSvc: ISimulatorCreditFixPort?=null, private val navController: NavController?=null){

    var item = mutableStateOf<SimulatorCreditDTO?>(null)

    init{
        dto?.let{
            item.value = it
        }
    }

    fun gotoAmortization(dto:SimulatorCreditDTO){
        item.value?.let{ record->
        navController?.let{
            simulatorCreditSvc?.let {
                it.setSimulation(record)
            }
            val path = it.context.getString(R.string.navigate_form_simulator_amortization_fix,dto.code).toUri()
            it.navigate(path)
        }
            }?:Toast.makeText(context,R.string.can_not_gotto_amortization, Toast.LENGTH_SHORT).show()
    }
}