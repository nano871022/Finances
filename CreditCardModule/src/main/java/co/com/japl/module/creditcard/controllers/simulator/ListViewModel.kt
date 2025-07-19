package co.com.japl.module.creditcard.controllers.simulator

import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val simulatorCreditVariablePort: ISimulatorCreditVariablePort) : ViewModel() {
}
