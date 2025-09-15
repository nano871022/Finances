package co.com.japl.module.credit.controllers.list

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.CreditList
import co.com.japl.module.credit.pojo.CreditPeriodGraceDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class ListViewModel constructor(private val savedStateHandle: SavedStateHandle,private val creditSvc:ICreditPort?,val periodGraceSvc:IPeriodGracePort?) : ViewModel() {

    val progress = mutableStateOf(true)
    val list = mutableListOf<CreditPeriodGraceDTO>()
    var period:YearMonth = YearMonth.now()
    private set

    init{
        savedStateHandle.get<YearMonth>("period")?.let { period = it }
    }

    fun delete(id:Int,navController: NavController){
       creditSvc?.let{
           if(it.delete(id)){
               Toast.makeText(navController.context, R.string.toast_successful_deleted, Toast.LENGTH_LONG).show().apply {
                   progress.value = true
               }
           }else{
               Toast.makeText(navController.context, R.string.toast_dont_successful_deleted, Toast.LENGTH_LONG).show()
           }
       }
    }

    fun deletePeriodGrace(id:Int,navController: NavController){
        periodGraceSvc?.let{
            if(it.delete(id)){
                Toast.makeText(navController.context, R.string.toast_period_grace_successful_deleted, Toast.LENGTH_LONG).show().apply {
                    progress.value = true
                }
            }else{
                Toast.makeText(navController.context, R.string.toast_period_grace_dont_successful_deleted, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun amortization(id:Int,navController: NavController){
        val credit = list.first { credit -> credit.credit.id == id }
        CreditList.amortization(credit.credit,LocalDate.now(),navController)
    }

    fun periodGrace(codeCredit:Int,period:Int,startDate: LocalDate,endDate: LocalDate,navController: NavController){
        val dto = GracePeriodDTO(0,startDate,endDate,codeCredit.toLong(),period.toShort())
        periodGraceSvc?.let{
            if(it.add(dto)){
                Toast.makeText(navController.context, R.string.toast_period_grace_successful_added, Toast.LENGTH_LONG).show().apply {
                    progress.value = true
                }
            }else{
                Toast.makeText(navController.context, R.string.toast_period_grace_dont_successful_added, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun additional(id:Int,navController: NavController){
        CreditList.additional(id,navController)
    }

    fun execute() {
        CoroutineScope(Dispatchers.IO).launch {
            load()
        }
    }

    suspend fun load(){
        progress.value = true
        creditSvc?.getCreditEnable(period)?.takeIf { it.isNotEmpty() }?.let{
            list.clear()
            val credits = it.map {
                CreditPeriodGraceDTO(it, periodGraceSvc?.hasGracePeriod(it.id) ?: false)
            }.sortedByDescending { it.credit.date }
            list.addAll(credits)
        }
        progress.value = false
    }
}