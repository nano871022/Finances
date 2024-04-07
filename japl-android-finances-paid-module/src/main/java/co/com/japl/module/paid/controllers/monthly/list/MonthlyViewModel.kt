package co.com.japl.module.paid.controllers.monthly.list

import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.navigations.Paid
import co.com.japl.module.paid.navigations.Paids
import co.com.japl.module.paid.navigations.Period
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import kotlinx.coroutines.runBlocking
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MonthlyViewModel constructor(private val period:YearMonth,private val paidSvc: IPaidPort?, private val incomesSvc:IInputPort?,private val accountSvc:IAccountPort?, private val smsSvc:ISMSPaidPort?,private val paidSmsSvc:ISmsPort?,private val prefs:Prefs?,private val navController: NavController?): ViewModel() {
    private var _accounts : List<AccountDTO>? = null
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
                    Paids.navigate(it.id,period, navController)
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
        progressStatus.value = 0.8f
        readSms()
        progressStatus.value = 1.0f
    }

    suspend fun execute() {
        periodState.value = "${period?.month?.getDisplayName(TextStyle.FULL, Locale("es","CO"))} ${period?.year}"
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
               period?.let {_period->
                   it.getRecap(codeAccount = account.id, period = _period)?.let { recap ->
                       countState.value = recap.count
                       paidTotalState.value = recap.totalPaid

                       progressStatus.value = 0.6f
                   }
                   it.getListGraph(codeAccount = account.id, period = _period).takeIf{it.isNotEmpty()}?.forEach (listGraph::add)
               }

            }
            incomesSvc?.let {
                period?.let { _period ->
                    it.getTotalInputs(account.id, _period)?.let { total ->
                        incomesTotalState.value = total
                        progressStatus.value = 0.7f
                    }
                }
            }

        }
        loaderState.value = false
    }

    suspend fun readSms(){
        try {
            _accounts?.takeIf { it.isNotEmpty() }?.forEach { dto ->
                    smsSvc?.getAllByCodeAccount(dto.id)
                    ?.forEach { sms ->
                        smsSvc?.getSmsMessages(sms.phoneNumber, sms.pattern,prefs?.paidSMSDaysRead?:0)
                            .takeIf { it?.isNotEmpty() == true }
                            ?.forEach {
                                paidSmsSvc?.createBySms(
                                    name = it.first,
                                    value = it.second,
                                    date = it.third,
                                    codeAccount = dto.id
                                )
                            }
                    }
            }
        }catch (e:Exception){
            Log.e(javaClass.name,e.message,e)
        }
    }

}