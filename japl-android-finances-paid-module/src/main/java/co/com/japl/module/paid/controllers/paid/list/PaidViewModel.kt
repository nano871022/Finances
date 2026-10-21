package co.com.japl.module.paid.controllers.paid.list

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.enums.PaidListOptions
import co.com.japl.module.paid.navigations.Paid
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.NumbersUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@HiltViewModel(assistedFactory = PaidViewModel.Factory::class)
class PaidViewModel @AssistedInject constructor(@Assisted private val accountCode:Int, @Assisted private val period:YearMonth, private val paidSvc:IPaidPort?=null,
                    val prefs:Prefs?, @Assisted private val navController: NavController? = null, private val emailSvc:IEmailPaidPort? = null, private val paidSmsSvc:ISmsPort? = null, private val smsPaidSvc:ISMSPaidPort? = null): ViewModel(){

    @AssistedFactory
    interface Factory {
        fun create(accountCode: Int, period: YearMonth, navController: NavController?): PaidViewModel
    }

    val progressStatus = mutableFloatStateOf(0.0f)
    val loaderState = mutableStateOf(true)

    val list = mutableStateMapOf<YearMonth,List<PaidDTO>>()

    val periodOfList = mutableStateOf("")
    val allValues = mutableStateOf(0.0)
    val paidEmailDaysRead = mutableStateOf("${prefs?.paidEmailDaysRead ?: 7}")
    val paidSMSDaysRead = mutableStateOf("${prefs?.paidSMSDaysRead ?: 7}")
    val errorPaidEmailDaysRead = mutableStateOf(false)
    val errorPaidSMSDaysRead = mutableStateOf(false)

    val settingState = mutableStateOf(false)

    fun hasGmailPermission(context: Context): Boolean {
        return emailSvc?.isEmailAccessGranted() ?: false
    }

    fun hasActiveSms(): Boolean {
        return smsPaidSvc?.hasActiveSms() ?: false
    }

    fun hasActiveEmail(): Boolean {
        return emailSvc?.isEmailAccessGranted() == true && (emailSvc?.hasActiveEmail() == true)
    }

    fun newOne(){
        navController?.let {
            Paid.navigate(accountCode, it)
        }
    }

    fun delete(id:Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                paidSvc?.delete(id) ?: false
            }
            if(result){
                navController?.let{
                    Toast.makeText(it.context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show()
                        .also { loaderState.value = true }
                }
            }else{
                navController?.let{
                    Toast.makeText(it.context, R.string.toast_unsuccessful_deleted,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun edit(id:Int){
        navController?.let {
            Paid.navigate(id,accountCode,it)
        }
    }

    fun endRecurrent(id:Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                paidSvc?.endRecurrent(id) ?: false
            }
            if(result){
                        navController?.let{ Toast.makeText(it.context, R.string.toast_successful_end_recurrent,Toast.LENGTH_LONG).show().also {
                            loaderState.value = true
                        }}
            }else{
                navController?.let{ Toast.makeText(it.context, R.string.toast_unsuccessful_end_recurrent,Toast.LENGTH_LONG).show() }
            }
        }
    }

    fun copy(id:Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                paidSvc?.copy(id) ?: false
            }
            if(result){
                navController?.let{ Toast.makeText(it.context, R.string.toast_successful_copy,Toast.LENGTH_LONG).show().also {
                    loaderState.value = true
                }}
            }else{
                navController?.let{ Toast.makeText(it.context, R.string.toast_unsuccessful_copy,Toast.LENGTH_LONG).show() }
            }
        }
    }

    fun readEmail(context: Context){
        if (!hasActiveEmail()) return
        settingState.value = false
        viewModelScope.launch(Dispatchers.IO) {
            readEmail()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.email_read_process_completed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveSettings(context: Context) {
        if((!hasActiveSms() || !errorPaidSMSDaysRead.value) && (!hasActiveEmail() || !errorPaidEmailDaysRead.value)) {
            if (hasActiveSms()) {
                paidSMSDaysRead.value.toIntOrNull()?.let {
                    prefs?.paidSMSDaysRead = it
                }
            }
            if (hasActiveEmail()) {
                paidEmailDaysRead.value.toIntOrNull()?.let {
                    prefs?.paidEmailDaysRead = it
                }
            }
            settingState.value = false
            Toast.makeText(context, R.string.toast_saves_successful, Toast.LENGTH_SHORT).show()
        }
    }

    fun validation(){
        if (hasActiveEmail()) {
            paidEmailDaysRead.value.takeIf { it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
                errorPaidEmailDaysRead.value = false
            }?: errorPaidEmailDaysRead.let{it.value = true}
        }

        if (hasActiveSms()) {
            paidSMSDaysRead.value.takeIf { it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
                errorPaidSMSDaysRead.value = false
            }?: errorPaidSMSDaysRead.let{it.value = true}
        }
    }

    suspend fun readEmail(){
        if (!hasActiveEmail()) return
        val numDaysRead = paidEmailDaysRead.value.toIntOrNull() ?: prefs?.paidEmailDaysRead ?: 7
        try {
            emailSvc?.read(numDaysRead)
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message, e)
        }
    }

    fun listOptions(dto: PaidDTO):List<PaidListOptions>{
        var list = PaidListOptions.values().toList()
        if(!dto.recurrent){
           list =  PaidListOptions.values().toList().filter { it != PaidListOptions.END }
        }
        if(!dto.datePaid.isAfter(LocalDateTime.now().minusMonths(2))){
            list = list.filter { it != PaidListOptions.EDIT }
        }
        return list
    }

    fun main() {
        periodOfList.value = period.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "CO")))
        progressStatus.value = 0.0f
        loaderState.value = true
        viewModelScope.launch {
            try {
                execute()
            } catch (e: Exception) {
                Log.e(javaClass.name, "Error in main: ${e.message}", e)
            } finally {
                progressStatus.value = 1.0f
                loaderState.value = false
            }
        }
    }

    suspend fun execute() {
        try {
            paidSvc?.let { svcPort ->
                withContext(Dispatchers.IO) {
                    svcPort.get(accountCode, period)
                }.takeIf { it.isNotEmpty() }?.let {
                    list.clear()
                    progressStatus.value = 0.3f

                    list.putAll(it.sortedByDescending { dto -> dto.datePaid }.groupBy { dto -> YearMonth.of(dto.datePaid.year, dto.datePaid.monthValue) })
                    progressStatus.value = 0.5f
                    it.sumOf { dto -> dto.itemValue }.let { sum -> allValues.value = sum }
                    progressStatus.value = 0.8f
                }
            }
        } catch (e: Exception) {
            Log.e(javaClass.name, "Error in execute: ${e.message}", e)
        } finally {
            loaderState.value = false
        }
    }

}
