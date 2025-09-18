package co.com.japl.module.paid.controllers.period.list

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.com.japl.module.paid.navigations.Paids
import kotlinx.coroutines.runBlocking
import java.time.YearMonth

class PeriodsViewModel constructor(
    private val paidSvc: IPeriodPaidPort?,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var codeAccount: Int? = null

    val progress = mutableStateOf(0f)
    val loader = mutableStateOf(true)

    val list = mutableStateMapOf<Int, List<PeriodPaidDTO>>()

    init {
        savedStateHandle.get<Int>("codeAccount")?.let {
            codeAccount = it
        }
    }

    fun goToListDetail(period: YearMonth, navController: NavController) {
        codeAccount?.let { Paids.navigate(it, period, navController) }
    }

    fun main() = runBlocking{
        progress.value = 0.0f
        execute()
        progress.value = 1.0f
    }

    suspend fun execute() {

        paidSvc?.let{
            progress.value = 0.2f
            codeAccount?.let{paidSvc.get(codeAccount)}?.takeIf { it.isNotEmpty() }?.let{
                progress.value = 0.5f
                list.putAll(it.groupBy { it.date.year })
                progress.value = 0.8f
            }
            loader.value = false
            progress.value = 0.9f
        }

    }
}