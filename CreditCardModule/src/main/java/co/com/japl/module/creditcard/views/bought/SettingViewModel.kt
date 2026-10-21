package co.com.japl.module.creditcard.views.bought

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.ui.Prefs
import co.com.japl.module.creditcard.R
import co.com.japl.ui.utils.NumbersUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(private val prefs: Prefs, private val emailCCSvc: IEmailCreditCardPort, private val smsSvc: ISMSCreditCardPort ): ViewModel() {

    val state = mutableStateOf(false)

    fun hasGmailPermission(context: Context): Boolean {
        return emailCCSvc.isEmailAccessGranted()
    }

    fun hasActiveSms(): Boolean {
        return smsSvc.hasActiveSms()
    }

    fun hasActiveEmail(): Boolean {
        return emailCCSvc.isEmailAccessGranted() && emailCCSvc.hasActiveEmail()
    }

    val daysSmsRead = mutableStateOf("${prefs.creditCardSMSDaysRead}")
    val errorDaysSmsRead = mutableStateOf(false)
    val daysEmailRead = mutableStateOf("${prefs.creditCardEmailDaysRead}")
    val errorDaysEmailRead = mutableStateOf(false)
    val simulatorState = mutableStateOf(prefs.simulator)

    fun save(context:Context){
        if((!hasActiveSms() || !errorDaysSmsRead.value) && (!hasActiveEmail() || !errorDaysEmailRead.value)) {
            prefs.simulator = simulatorState.value
            if (hasActiveSms()) {
                prefs.creditCardSMSDaysRead = daysSmsRead.value.toInt()
            }
            if (hasActiveEmail()) {
                prefs.creditCardEmailDaysRead = daysEmailRead.value.toInt()
            }
            state.value = false
            Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show()
        }
    }

    fun validation(){
        if (hasActiveSms()) {
            daysSmsRead.value.takeIf { it != null && it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
                errorDaysSmsRead.value = false
            }?: errorDaysSmsRead.let{it.value = true}
        }

        if (hasActiveEmail()) {
            daysEmailRead.value.takeIf { it != null && it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
                errorDaysEmailRead.value = false
            }?: errorDaysEmailRead.let{it.value = true}
        }
    }

    fun readEmail(context: Context) {
        if (!hasActiveEmail()) return
        val numDaysRead = daysEmailRead.value.toIntOrNull() ?: prefs.creditCardEmailDaysRead

        viewModelScope.launch(Dispatchers.IO) {
            emailCCSvc.read(numDaysRead)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.email_read_process_completed, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
