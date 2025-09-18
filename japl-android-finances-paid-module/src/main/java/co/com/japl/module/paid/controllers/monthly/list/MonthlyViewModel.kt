package co.com.japl.module.paid.controllers.monthly.list

import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.module.paid.navigations.Paid
import co.com.japl.module.paid.navigations.Paids
import co.com.japl.module.paid.navigations.Period
import co.com.japl.ui.Prefs
import kotlinx.coroutines.runBlocking
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MonthlyViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val paidSvc: IPaidPort,
    private val incomesSvc: IInputPort,
    private val accountSvc: IAccountPort,
    private val smsSvc: ISMSPaidPort,
    private val paidSmsSvc: ISmsPort,
    private val prefs: Prefs
) : ViewModel() {
    private var _accounts: List<AccountDTO>? = null
    val listAccount get() = _accounts
    private var period: YearMonth = YearMonth.now()

    val loaderState = mutableStateOf(true)
    val progressStatus = mutableFloatStateOf(0.0f)
    val accountList = mutableStateListOf<Pair<Int, String>>()
    val accountState = mutableStateOf<AccountDTO?>(null)
    val periodState = mutableStateOf("")
    val countState = mutableIntStateOf(0)
    val paidTotalState = mutableDoubleStateOf(0.0)
    val incomesTotalState = mutableDoubleStateOf(0.0)
    val listGraph = mutableStateListOf<Pair<String, Double>>()

    init {
        savedStateHandle.get<String>("period")?.let {
            period = YearMonth.parse(it)
        }
        main()
    }

    fun goToListDetail(navController: NavController) {
        try {
            accountState.value?.let {
                Paids.navigate(it.id, period, navController)
            }
        } catch (e: Exception) {
            Log.e("MonthlyViewModel", e.message.toString())
        }
    }

    fun goToListPeriod(navController: NavController) {
        try {
            accountState.value?.let {
                Period.navigate(it.id, navController)
            }
        } catch (e: Exception) {
            Log.e("MonthlyViewModel", e.message.toString())
        }
    }

    fun goToListCreate(navController: NavController) {
        try {
            accountState.value?.let {
                Paid.navigate(it.id, navController)
            }
        } catch (e: Exception) {
            Log.e("MonthlyViewModel", e.message.toString())
        }
    }

    private fun main() = runBlocking {
        progressStatus.value = 0.0f
        execute()
        progressStatus.value = 0.8f
        readSms()
        progressStatus.value = 1.0f
    }

    private suspend fun execute() {
        periodState.value = "${period.month.getDisplayName(TextStyle.FULL, Locale("es", "CO"))} ${period.year}"
        accountSvc.getAllActive().takeIf { it.isNotEmpty() }?.let { list ->
            accountList.clear()
            _accounts = list
            list.forEach {
                accountList.add(Pair(it.id, it.name))
            }
            list.takeIf { it.isNotEmpty() && it.size == 1 }?.let {
                accountState.value = it.first()
            }
            progressStatus.value = 0.3f
        }

        accountState.value?.let { account ->
            paidSvc.getRecap(codeAccount = account.id, period = period)?.let { recap ->
                countState.value = recap.count
                paidTotalState.value = recap.totalPaid
                progressStatus.value = 0.6f
            }
            paidSvc.getListGraph(codeAccount = account.id, period = period).takeIf { it.isNotEmpty() }
                ?.forEach(listGraph::add)
            incomesSvc.getTotalInputs(account.id, period)?.let { total ->
                incomesTotalState.value = total
                progressStatus.value = 0.7f
            }
        }
        loaderState.value = false
    }

    private suspend fun readSms() {
        try {
            _accounts?.takeIf { it.isNotEmpty() }?.forEach { dto ->
                smsSvc.getAllByCodeAccount(dto.id)
                    ?.forEach { sms ->
                        smsSvc.getSmsMessages(sms.phoneNumber, sms.pattern, prefs.paidSMSDaysRead)
                            .takeIf { it?.isNotEmpty() == true }
                            ?.forEach {
                                paidSmsSvc.createBySms(
                                    name = it.first,
                                    value = it.second,
                                    date = it.third,
                                    codeAccount = dto.id
                                )
                            }
                    }
            }
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message, e)
        }
    }

    companion object {
        fun create(
            extras: CreationExtras,
            paidSvc: IPaidPort,
            incomesSvc: IInputPort,
            accountSvc: IAccountPort,
            smsSvc: ISMSPaidPort,
            paidSmsSvc: ISmsPort,
            prefs: Prefs
        ): MonthlyViewModel {
            val savedStateHandle = extras.createSavedStateHandle()
            return MonthlyViewModel(
                savedStateHandle,
                paidSvc,
                incomesSvc,
                accountSvc,
                smsSvc,
                paidSmsSvc,
                prefs
            )
        }
    }
}