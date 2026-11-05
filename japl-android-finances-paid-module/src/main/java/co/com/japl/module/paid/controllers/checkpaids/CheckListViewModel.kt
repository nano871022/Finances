package co.com.japl.module.paid.controllers.checkpaids

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.com.japl.module.paid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class CheckListViewModel @Inject constructor(private val period:YearMonth, private val svc:ICheckPaymentPort?): ViewModel() {
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
            reload()
            loaderProgressStatus.value = false
        }

    }

    fun remove(dto:CheckPaymentDTO){
        svc?.delete(dto)?.takeIf { it }?.let {
            loaderProgressStatus.value = true
            reload()
            loaderProgressStatus.value = false
        }
    }

    fun reload() = viewModelScope.launch {
        loaderStatus.value = true
        execute()
    }

    suspend fun execute(){
        val currentDate = LocalDateTime.now()
        val payments = withContext(Dispatchers.IO) {
            svc?.getCheckPayments(period)
        }
        payments?.takeIf { it.isNotEmpty() }?.let {
            listState.clear()
            listState.addAll(it)
            listState.forEach {
                val day = it.date?.dayOfMonth ?: 1
                it.date = currentDate
                if (day > 28) {
                    val dayOfMonth =
                        LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1).dayOfMonth
                    if (dayOfMonth > day) {
                        it.date = it.date?.withDayOfMonth(day)
                    } else {
                        it.date = it.date?.withDayOfMonth(dayOfMonth)
                    }
                }else{
                    it.date = it.date?.withDayOfMonth(day)
                }
            }
        }
        loaderStatus.value = false
    }
}

