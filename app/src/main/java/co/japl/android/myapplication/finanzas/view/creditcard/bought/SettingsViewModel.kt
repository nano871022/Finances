package co.japl.android.myapplication.finanzas.view.creditcard.bought

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R
import co.japl.finances.core.utils.NumbersUtil

class SettingsViewModel constructor(private val prefs: Prefs): ViewModel() {

    val state = mutableStateOf(false)

    val daysSmsRead = mutableStateOf("${prefs.creditCardSMSDaysRead}")
    val errorDaysSmsRead = mutableStateOf(false)
    val simulatorState = mutableStateOf(prefs.simulator)

    fun save(context:Context){
        if(!errorDaysSmsRead.value) {
            prefs.simulator = simulatorState.value
            prefs.creditCardSMSDaysRead = daysSmsRead.value.toInt()
            state.value = false
            Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show()
        }
    }

    fun validation(){
        daysSmsRead.value.takeIf { it != null && it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
            errorDaysSmsRead.value = false
        }?: errorDaysSmsRead.let{it.value = true}
    }

}