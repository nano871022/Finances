package co.com.japl.module.creditcard.controllers.simulator

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.creditcard.R

class SimulatorListItemViewModel(private val context: Context, private val itemValue: SimulatorCreditDTO?=null, private val simulatorVariableSvc: ISimulatorCreditVariablePort?=null, private val navController: NavController?=null){

    val showOptions = mutableStateOf(false)
    var item= mutableStateOf<SimulatorCreditDTO?> (null)


    init{
        itemValue?.let{
            item.value = it
            Log.d(this.javaClass.name,"Item: $it")
        }
    }

    fun gotoAmortization(code:Int){
        item.value?.let { record->
            navController?.let {
                simulatorVariableSvc?.let {
                    it.setSimulation(record)
                }
                val path = navController.context.getString(
                    R.string.navigate_form_simulator_amortization,
                    code
                ).toUri()
                it.navigate(path)
            }
        }?:{
            Toast.makeText(context,R.string.can_not_goto_amortization, Toast.LENGTH_SHORT).show()
        }
    }
}