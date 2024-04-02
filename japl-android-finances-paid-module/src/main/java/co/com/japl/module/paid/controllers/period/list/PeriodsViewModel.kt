package co.com.japl.module.paid.controllers.period.list

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.com.japl.module.paid.navigations.Monthly
import kotlinx.coroutines.runBlocking
import java.time.YearMonth

class PeriodsViewModel constructor(private val paidSvc: IPeriodPaidPort?, private val navController: NavController?): ViewModel(){
    val progress = mutableStateOf(0f)
    val loader = mutableStateOf(true)

    val list = mutableStateMapOf<Int,List<PeriodPaidDTO>>()

    fun goToMonthly(period:YearMonth){
        navController?.let {
            Monthly.navigate(period,navController)
        }
    }

    fun main() = runBlocking{
        progress.value = 0.0f
        execute()
        progress.value = 1.0f
    }

    suspend fun execute() {

        paidSvc?.let{
            progress.value = 0.2f
            it.get().takeIf { it.isNotEmpty() }?.let{
                progress.value = 0.5f
                list.putAll(it.groupBy { it.date.year })
                progress.value = 0.8f
            }
            loader.value = false
            progress.value = 0.9f
        }

    }
}