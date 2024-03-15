package co.japl.android.myapplication.finanzas.view.creditcard.bought

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.com.japl.ui.Prefs

class SettingsViewModel constructor(private val prefs: Prefs): ViewModel() {

    val state = mutableStateOf(false)

    val simulatorState = mutableStateOf(prefs.simulator)

    fun save(){
        prefs.simulator = simulatorState.value
        state.value = false
    }

}