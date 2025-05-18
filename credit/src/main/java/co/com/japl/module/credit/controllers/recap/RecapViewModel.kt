package co.com.japl.module.credit.controllers.recap

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.module.credit.navigations.CreditList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class RecapViewModel @Inject constructor(private val creditsSvc:ICreditPort?, val yearMonth:YearMonth,private val navController: NavController?) : ViewModel() {
    val loader = mutableStateOf(true)
    val progress = mutableFloatStateOf(0f)
    val listCredits = mutableListOf<RecapCreditDTO>()

    fun addCredit() {
        navController?.let( CreditList::addCredit)
    }

    fun detailCredits() {
        navController?.let(CreditList::detailCredits)
    }

    fun periodCredits() {
        navController?.let(CreditList::periodCredits)
    }

    fun execute() = CoroutineScope(Dispatchers.IO).launch {
        progress.floatValue = 0.1f
        load()
        progress.floatValue = 1f
    }

    suspend fun load(){
        creditsSvc?.let{
            progress.floatValue = 0.2f
            it.getCreditsEnables(yearMonth).takeIf { it.isNotEmpty() }?.let{
                progress.floatValue = 0.7f
                listCredits.clear()
                progress.floatValue = 0.8f
                listCredits.addAll(it)
            }
            progress.floatValue = 0.9f
            loader.value = false
        }
    }

}