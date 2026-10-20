package co.com.japl.module.creditcard.controllers.paid

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.navigations.Bought
import co.com.japl.ui.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class BoughtCreditCardViewModel(
    private val service: IBoughtListPort?,
    private val idCreditCard: Int,
    private val navController: NavController? = null,
    private val prefs: Prefs
) : ViewModel() {
    val cache = mutableStateOf(prefs.simulator)
    val progress = mutableFloatStateOf(0f)
    val loader = mutableStateOf(false)
    private var _list: Map<Long, List<BoughtCreditCardPeriodDTO>> = mapOf()
    val periodList get() = _list

    fun main() {
        progress.floatValue = 0.2f
        loader.value = false
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    execute()
                }
            } catch (e: Exception) {
                Log.e(javaClass.name, "Error in BoughtCreditCardViewModel main: ${e.message}", e)
            } finally {
                progress.floatValue = 1.0f
                loader.value = true
            }
        }
    }

    suspend fun execute() {
        try {
            service?.getBoughtPeriodList(idCreditCard, cache.value)?.takeIf { it.isNotEmpty() }?.let {
                progress.floatValue = 0.7f
                _list = it.groupBy { period -> period.periodEnd.year.toLong() }
            }
        } catch (e: Exception) {
            Log.e(javaClass.name, "Error in execute: ${e.message}", e)
        } finally {
            progress.floatValue = 1.0f
            loader.value = true
        }
    }

    fun goToListDetail(cutOffDay: Short, cutOff: LocalDateTime) {
        navController?.let { Bought.navigate(idCreditCard, cutOffDay, cutOff, it) }
    }
}
