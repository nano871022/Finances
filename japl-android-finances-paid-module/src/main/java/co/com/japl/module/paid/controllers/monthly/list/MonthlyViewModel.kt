package co.com.japl.module.paid.controllers.monthly.list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.module.paid.navigations.Paid
import co.com.japl.module.paid.navigations.Paids
import co.com.japl.module.paid.navigations.Period
import kotlinx.coroutines.runBlocking
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MonthlyViewModel constructor(private val paidSvc: IPaidPort?, private val incomesSvc:IInputPort?,private val accountSvc:IAccountPort?, private val navController: NavController?): ViewModel() {
    private var _accounts : List<AccountDTO>? = null
    private var _period: YearMonth? = null
    val listAccount get() = _accounts

    val loaderState = mutableStateOf(true)
    val progressStatus = mutableFloatStateOf(0.0f)
    val accountList = mutableStateListOf<Pair<Int,String>>()
    val accountState = mutableStateOf<AccountDTO?>(null)
    val periodState = mutableStateOf("")
    val countState = mutableIntStateOf(0)
    val paidTotalState = mutableDoubleStateOf(0.0)
    val incomesTotalState = mutableDoubleStateOf(0.0)
    val listGraph = mutableStateListOf<Pair<String,Double>>()

    fun goToListDetail(){
        try {
            navController?.let {
                accountState.value?.let {
                    Paids.navigate(it.id, navController)
                }
            }
        }catch (e:Exception){
            Log.e("MonthlyViewModel",e.message.toString())
        }
    }

    fun goToListPeriod(){
        try {
            navController?.let {
                accountState.value?.let {

                    Period.navigate(it.id, navController)
                }
            }
        }catch (e:Exception){
            Log.e("MonthlyViewModel",e.message.toString())
        }
    }

    fun goToListCreate(){
        try {


            navController?.let {
                accountState.value?.let {
                    Paid.navigate(it.id, navController)
                }
            }
        }catch (e:Exception){
            Log.e("MonthlyViewModel",e.message.toString())
        }
    }


    fun main()= runBlocking {
        progressStatus.value = 0.0f
        execute()
        progressStatus.value = 1.0f
    }

    suspend fun execute() {
        _period = YearMonth.now()
        periodState.value = "${_period?.month?.getDisplayName(TextStyle.FULL, Locale("es","CO"))} ${_period?.year}"
        accountSvc?.let {
            it.getAllActive().takeIf { it.isNotEmpty() }?.let { list ->
                accountList.clear()
                _accounts = list
                list.forEach {
                    accountList.add(Pair(it.id,it.name))
                }
                list.takeIf { it.isNotEmpty() && it.size == 1 }?.let {
                    accountState.value = it.first()
                }
                progressStatus.value = 0.3f
            }
        }

        accountState.value?.let {account->
            paidSvc?.let {
               _period?.let {_period->
                   it.getRecap(codeAccount = account.id, period = _period)?.let { recap ->
                       countState.value = recap.count
                       paidTotalState.value = recap.totalPaid

                       progressStatus.value = 0.6f
                   }
                   it.getListGraph(codeAccount = account.id, period = _period).takeIf{it.isNotEmpty()}?.forEach (listGraph::add)
               }

            }
            incomesSvc?.let {
                _period?.let { _period ->
                    it.getTotalInputs(account.id, _period)?.let { total ->
                        incomesTotalState.value = total
                        progressStatus.value = 0.9f
                    }
                }
            }

        }
        loaderState.value = false
    }

}