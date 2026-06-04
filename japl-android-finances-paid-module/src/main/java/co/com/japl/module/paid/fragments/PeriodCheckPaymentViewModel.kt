package co.com.japl.module.paid.fragments

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.inbounds.paid.ICheckPaymentPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PeriodCheckPaymentViewModel(private val periodCheckPaymentSvc:co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort? ):ViewModel() {

    val loader = mutableStateOf(true)

    val map = mutableStateMapOf<Int,List<PeriodCheckPaymentDTO>>()


    fun execute()=viewModelScope.launch{
        periodCheckPaymentSvc?.let{
            withContext(Dispatchers.IO){
                it.getPeriodsPayment()
            }.takeIf { it.isNotEmpty() }?.let{
                map.putAll(it.groupBy{ it.period.year })
            }
        }
        loader.value = false
    }
}