package co.com.japl.module.creditcard.controllers.paid

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.navigations.Bought
import co.com.japl.ui.Prefs
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

import androidx.lifecycle.SavedStateHandle

class BoughtCreditCardViewModel constructor(private val service:IBoughtListPort?,private val prefs:Prefs,private val savedStateHandle: SavedStateHandle):ViewModel() {
    val cache = mutableStateOf(prefs.simulator)
    val progress = mutableFloatStateOf(0f)
    val loader = mutableStateOf(false)
    private var _list:Map<Long,List<BoughtCreditCardPeriodDTO>> = mapOf()
    private var idCreditCard:Int = 0
    val periodList get() = _list
    init{
        savedStateHandle.get<Int>("idCreditCard")?.let{
            idCreditCard = it
        }
    }
    fun main() = runBlocking {
        progress.floatValue = 0.2f
        execute()
    }

    suspend fun execute(){
        service?.getBoughtPeriodList(idCreditCard,cache.value)?.takeIf { it.isNotEmpty() }?.let{
            progress.floatValue = 0.7f
            _list = it.groupBy { it.periodEnd.year.toLong() }
        }
        progress.floatValue = 1.0f
        loader.value = true
    }

    fun goToListDetail(cutOffDay:Short,cutOff:LocalDateTime,navController: NavController){
        Bought.navigate(idCreditCard,cutOffDay,cutOff, navController)
    }


}