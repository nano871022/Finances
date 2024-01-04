package co.com.japl.module.creditcard.controllers.paid

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.navigations.ListBought
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class BoughtCreditCardViewModel constructor(private val service:IBoughtListPort?,private val idCreditCard:Int,private val navController: NavController):ViewModel() {

    val progress = mutableFloatStateOf(0f)
    val loader = mutableStateOf(false)
    private var _list:Map<Long,List<BoughtCreditCardPeriodDTO>> = mapOf()
    val periodList get() = _list

    fun main() = runBlocking {
        progress.floatValue = 0.2f
        execute()
    }

    suspend fun execute(){
        service?.getBoughtPeriodList(idCreditCard)?.takeIf { it.isNotEmpty() }?.let{
            progress.floatValue = 0.7f
            _list = it.groupBy { it.periodEnd.year.toLong() }
        }
        progress.floatValue = 1.0f
        loader.value = true
    }

    fun goToListDetail(cutOffDay:Short,cutOff:LocalDateTime){
        ListBought.navigate(idCreditCard,cutOffDay,cutOff, navController)
    }


}