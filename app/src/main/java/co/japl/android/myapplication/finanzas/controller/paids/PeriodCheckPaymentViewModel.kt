package co.japl.android.myapplication.finanzas.controller.paids

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import kotlinx.coroutines.runBlocking
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort

class PeriodCheckPaymentViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val periodCheckPaymentSvc:ICheckPaymentPort? ):ViewModel() {

    val progression = mutableFloatStateOf(0.0f)
    val loader = mutableStateOf(true)

    val map = mutableStateMapOf<Int,List<PeriodCheckPaymentDTO>>()

    fun main() = runBlocking {
        progression.value = 0.1f
        execute()
        progression.value = 1.0f
    }

    suspend fun execute(){
        val list = mutableListOf<PeriodCheckPaymentDTO>()
        progression.floatValue = 0.3f
        periodCheckPaymentSvc?.getPeriodsPayment()?.takeIf { it.isNotEmpty() }?.let( list::addAll )
        progression.floatValue = 0.6f
        list.takeIf { it.isNotEmpty() }?.let { map.putAll(list.groupBy{ it.period.year }) }
        progression.floatValue = 0.7f
        loader.value = false
        progression.floatValue = 0.8f
    }
}