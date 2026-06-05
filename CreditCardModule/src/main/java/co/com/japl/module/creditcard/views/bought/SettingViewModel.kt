package co.com.japl.module.creditcard.views.bought

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.ui.Prefs
import co.com.japl.module.creditcard.R
import co.com.japl.ui.utils.NumbersUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(private val prefs: Prefs,private val emailCCSvc: IEmailCreditCardPort ): ViewModel() {

    val state = mutableStateOf(false)

    val daysSmsRead = mutableStateOf("${prefs.creditCardSMSDaysRead}")
    val errorDaysSmsRead = mutableStateOf(false)
    val daysEmailRead = mutableStateOf("${prefs.creditCardEmailDaysRead}")
    val errorDaysEmailRead = mutableStateOf(false)
    val simulatorState = mutableStateOf(prefs.simulator)

    fun save(context:Context){
        if(!errorDaysSmsRead.value && !errorDaysEmailRead.value) {
            prefs.simulator = simulatorState.value
            prefs.creditCardSMSDaysRead = daysSmsRead.value.toInt()
            prefs.creditCardEmailDaysRead = daysEmailRead.value.toInt()
            state.value = false
            Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show()
        }
    }

    fun validation(){
        daysSmsRead.value.takeIf { it != null && it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
            errorDaysSmsRead.value = false
        }?: errorDaysSmsRead.let{it.value = true}

        daysEmailRead.value.takeIf { it != null && it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
            errorDaysEmailRead.value = false
        }?: errorDaysEmailRead.let{it.value = true}
    }

    fun readEmail(context: Context) {
        val numDaysRead = daysEmailRead.value.toIntOrNull() ?: prefs.creditCardEmailDaysRead

        viewModelScope.launch(Dispatchers.IO) {
            emailCCSvc.read(numDaysRead)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.email_read_process_completed, Toast.LENGTH_SHORT).show()
            }
        }
    }

}