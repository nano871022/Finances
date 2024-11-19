package co.japl.android.myapplication.finanzas.controller.paids

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.japl.android.myapplication.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.YearMonth

class CheckListViewModel constructor(private val period:YearMonth,private val svc:ICheckPaymentPort?): ViewModel() {
    val progression = mutableFloatStateOf(0.0f)
    val listState = mutableStateListOf<CheckPaymentDTO>()
    val loaderStatus = mutableStateOf(true)
    val loaderProgressStatus = mutableStateOf(false)

    fun save(context:Context){
        loaderProgressStatus.value = true
        listState.takeIf { it.isNotEmpty() }?.filter{
            it.id > 0 && it.update
        }?.forEach { svc?.update(it) }

        listState.takeIf { it.isNotEmpty() }?.filter{
            it.id == 0
        }?.forEach { svc?.save(it) }

        Toast.makeText(context, R.string.toast_save_successful, Toast.LENGTH_LONG).show().also {
            loaderStatus.value = true
            loaderProgressStatus.value = false
        }

    }

    fun remove(dto:CheckPaymentDTO){
            svc?.delete(dto)?.takeIf { it }?.let {
                loaderStatus.value = true
                loaderProgressStatus.value = true
                main()
                loaderProgressStatus.value = false
            }
    }
    
    fun main() = runBlocking {
        progression.floatValue = 0.1f
        execute()
        progression.floatValue = 1.0f
    }

    suspend fun execute(){
        progression.floatValue = 0.1f
        svc?.getCheckPayments(period)?.takeIf { it.isNotEmpty() }?.let {
            progression.floatValue = 0.6f
            listState.clear()
            progression.floatValue = 0.7f
            listState.addAll(it)
            progression.floatValue = 0.8f
        }
        loaderStatus.value = false
        progression.floatValue = 0.9f
    }
}