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
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.enums.PaidListOptions
import co.com.japl.module.paid.navigations.Paid
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaidViewModel constructor(private val accountCode:Int, private val period:YearMonth,private val paidSvc:IPaidPort?=null,public val prefs:Prefs?,private val navController: NavController? = null,private val emailSvc:IEmailPaidPort? = null,private val paidSmsSvc:ISmsPort?): ViewModel(){

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


    fun newOne(){
        navController?.let {
            Paid.navigate(accountCode, it)
        }
    }

    fun delete(id:Int){
        paidSvc?.let {
            if(it.delete(id)){
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
        paidSvc?.let{
            if(it.endRecurrent(id)){
                        navController?.let{ Toast.makeText(it.context, R.string.toast_successful_end_recurrent,Toast.LENGTH_LONG).show().also {
                            loaderState.value = true
                        }}
            }else{
                navController?.let{ Toast.makeText(it.context, R.string.toast_unsuccessful_end_recurrent,Toast.LENGTH_LONG).show() }
            }
        }

    }

    fun copy(id:Int){
        paidSvc?.let{
            if(it.copy(id)){
                navController?.let{ Toast.makeText(it.context, R.string.toast_successful_copy,Toast.LENGTH_LONG).show().also {
                    loaderState.value = true
                }}
            }else{
                navController?.let{ Toast.makeText(it.context, R.string.toast_unsuccessful_copy,Toast.LENGTH_LONG).show() }
            }
        }
    }

    fun readEmail(context: Context){
        settingState.value = false
        viewModelScope.launch(Dispatchers.IO) {
            readEmail()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.email_read_process_completed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveSettings(context: Context) {
        if(!errorPaidEmailDaysRead.value && !errorPaidSMSDaysRead.value) {
            paidEmailDaysRead.value.toIntOrNull()?.let {
                prefs?.paidEmailDaysRead = it
            }
            paidSMSDaysRead.value.toIntOrNull()?.let {
                prefs?.paidSMSDaysRead = it
            }
            settingState.value = false
            Toast.makeText(context, R.string.toast_saves_successful, Toast.LENGTH_SHORT).show()
        }
    }

    fun validation(){
        paidEmailDaysRead.value.takeIf { it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
            errorPaidEmailDaysRead.value = false
        }?: errorPaidEmailDaysRead.let{it.value = true}

        paidSMSDaysRead.value.takeIf { it.isNotEmpty() && NumbersUtil.isNumber(it) }?.let{
            errorPaidSMSDaysRead.value = false
        }?: errorPaidSMSDaysRead.let{it.value = true}
    }

    suspend fun readEmail(){
        val numDaysRead = paidEmailDaysRead.value.toIntOrNull() ?: prefs?.paidEmailDaysRead ?: 7
        try {
            emailSvc?.getAll()?.filter { it.active }?.forEach { emailConfig ->

                emailSvc.validateMessagePattern(emailConfig, numDaysRead).filter { it.matched }.forEach { validation ->

                    val date = validation.date?.let {
                        DateUtils.toLocalDate(it)
                    }?.atStartOfDay()

                    val value = validation.value?.toDoubleOrNull()
                    if (date != null && value != null) {
                        paidSmsSvc?.createBySms(
                            name = validation.name ?: "",
                            value = value,
                            date = date,
                            codeAccount = emailConfig.codeAccount
                        )
                    }
                }
            }
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

    fun main()= runBlocking {
        periodOfList.value  = period.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es","CO")))
        progressStatus.value = 0.0f
        execute()
        progressStatus.value = 1.0f
    }

    suspend fun execute() {
        paidSvc?.let{
            it.get(accountCode,period).takeIf { it.isNotEmpty() }?.let{
                list.clear()
                progressStatus.value = 0.3f

                list.putAll(it.sortedByDescending { it.datePaid }.groupBy { YearMonth.of(it.datePaid.year, it.datePaid.monthValue) })
                progressStatus.value = 0.5f
                it.sumOf { it.itemValue }.let{allValues.value = it}
                progressStatus.value = 0.8f


            }
        }
        loaderState.value = false
    }

}