package co.com.japl.module.credit.controllers.simulator

import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SimulatorViewModel @Inject constructor(private val simulatorCreditFixPort: ISimulatorCreditFixPort):ViewModel() {
}
