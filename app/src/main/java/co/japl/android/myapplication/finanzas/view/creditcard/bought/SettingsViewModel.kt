package co.japl.android.myapplication.finanzas.view.creditcard.bought

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import co.japl.finances.core.utils.DateUtils
import co.japl.finances.core.utils.NumbersUtil
import dagger.hilt.EntryPoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class SettingsViewModel(private val prefs: Prefs): ViewModel() {

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
        val emailCCSvc: IEmailCreditCardPort = EntryPoints.get(context.applicationContext, EntryPoint::class.java).getEmailCreditCardPort()
        val numDaysRead = daysEmailRead.value.toIntOrNull() ?: prefs.creditCardEmailDaysRead

        viewModelScope.launch(Dispatchers.IO) {
            emailCCSvc.read(numDaysRead)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.email_read_process_completed, Toast.LENGTH_SHORT).show()
            }
        }
    }

}