package co.com.japl.module.credit.controllers.list

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.CreditList
import co.com.japl.module.credit.pojo.CreditPeriodGraceDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import androidx.lifecycle.SavedStateHandle
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val creditsSvc:ICreditPort?, 
    val periodGraceSvc:IPeriodGracePort?,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val period: YearMonth = savedStateHandle.get<String>("PERIOD")?.let { YearMonth.parse(it) } ?: YearMonth.now()
    var navController: NavController? = null

    val progress = mutableStateOf(true)
    val list = mutableListOf<CreditPeriodGraceDTO>()

    fun delete(id:Int){
       creditsSvc?.let{
           if(it.delete(id)){
               Toast.makeText(navController?.context, R.string.toast_successful_deleted, Toast.LENGTH_LONG).show().apply {
                   progress.value = true
               }
           }else{
               Toast.makeText(navController?.context, R.string.toast_dont_successful_deleted, Toast.LENGTH_LONG).show()
           }
       }
    }

    fun edit(id:Int){
        navController?.let {
            CreditList.updCredit(id,it)
        }
    }

    fun create(){
        navController?.let {
            CreditList.addCredit(it)
        }
    }

    fun deletePeriodGrace(id:Int){
        periodGraceSvc?.let{
            if(it.delete(id)){
                Toast.makeText(navController?.context, R.string.toast_period_grace_successful_deleted, Toast.LENGTH_LONG).show().apply {
                    progress.value = true
                }
            }else{
                Toast.makeText(navController?.context, R.string.toast_period_grace_dont_successful_deleted, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun amortization(id:Int){
        val credit = list.first { credit -> credit.credit.id == id }
        navController?.let{CreditList.amortization(credit.credit,LocalDate.now(),navController!!)}
    }

    fun periodGrace(codeCredit:Int,period:Int,startDate: LocalDate,endDate: LocalDate){
        val dto = GracePeriodDTO(0,startDate,endDate,codeCredit.toLong(),period.toShort())
        periodGraceSvc?.let{
            if(it.add(dto)){
                Toast.makeText(navController?.context, R.string.toast_period_grace_successful_added, Toast.LENGTH_LONG).show().apply {
                    progress.value = true
                }
            }else{
                Toast.makeText(navController?.context, R.string.toast_period_grace_dont_successful_added, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun additional(id:Int){
        navController?.let{CreditList.additional(id,navController!!)}
    }

    fun getMonthPaid(max:Int,date:LocalDate):Long{
        val months = DateUtils.getMonths(
            date,
            LocalDateTime.now()
        )
        if(months > max){
            return max.toLong()
        }
        return months
    }

    fun execute() {
        CoroutineScope(Dispatchers.IO).launch {
            load()
        }
    }

    suspend fun load(){
        progress.value = true
        creditsSvc?.getCreditEnable(period)?.takeIf { it.isNotEmpty() }?.let{
            list.clear()
            val credits = it.map {
                CreditPeriodGraceDTO(it, periodGraceSvc?.hasGracePeriod(it.id) ?: false)
            }.sortedByDescending { it.credit.date }
            list.addAll(credits)
        }
        progress.value = false
    }
}
