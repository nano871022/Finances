package co.com.japl.module.paid.controllers.paid.list

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.navigations.Paid
import kotlinx.coroutines.runBlocking
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaidViewModel constructor(private val accountCode:Int, private val period:YearMonth,private val paidSvc:IPaidPort?=null,private val navController: NavController? = null): ViewModel(){

    val progressStatus = mutableFloatStateOf(0.0f)
    val loaderState = mutableStateOf(true)

    val list = mutableStateMapOf<YearMonth,List<PaidDTO>>()

    val periodOfList = mutableStateOf("")
    val allValues = mutableStateOf(0.0)

    fun newOne(){
        navController?.let {
            Paid.navigate(accountCode, it)
        }
    }

    fun delete(id:Int){
        paidSvc?.let {
            if(it.delete(id)){
                navController?.let{
                    Toast.makeText(it.context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show()
                        .also { loaderState.value = true }

                }
            }else{
                navController?.let{
                    Toast.makeText(it.context, R.string.toast_unsuccessful_deleted,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun edit(id:Int){
        navController?.let {
            Paid.navigate(id,accountCode,it)
        }
    }

    fun main()= runBlocking {
        periodOfList.value  = period.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es","CO")))
        progressStatus.value = 0.0f
        execute()
        progressStatus.value = 1.0f
    }

    suspend fun execute() {
        paidSvc?.let{
            it.get(accountCode,period).takeIf { it.isNotEmpty() }?.let{
                list.clear()
                progressStatus.value = 0.3f

                list.putAll(it.sortedByDescending { it.datePaid }.groupBy { YearMonth.of(it.datePaid.year, it.datePaid.monthValue) })
                progressStatus.value = 0.5f
                it.sumOf { it.itemValue }.let{allValues.value = it}
                progressStatus.value = 0.8f

                loaderState.value = false
            }
        }
    }

}