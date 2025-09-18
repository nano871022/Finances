package co.com.japl.module.paid.controllers.paid.form

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.R
import co.japl.android.graphs.utils.NumbersUtil
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PaidViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val accountSvc: IAccountPort,
    private val paidSvc: IPaidPort
) : ViewModel() {
    private var codeAccount: Int? = null
    private var codePaid: Int? = null
    private var _paid: PaidDTO? = null
    var accountList: List<AccountDTO>? = null
    val accountListPair = mutableStateListOf<Pair<Int, String>>()

    val loading = mutableStateOf(true)
    val progressStatus = mutableStateOf(0.0f)

    val account = mutableStateOf<AccountDTO?>(null)
    val errorAccount = mutableStateOf(false)
    val date = mutableStateOf<LocalDate?>(null)
    val errorDate = mutableStateOf(false)
    val name = mutableStateOf("")
    val errorName = mutableStateOf(false)
    val value = mutableStateOf("")
    val errorValue = mutableStateOf(false)
    val recurrent = mutableStateOf(false)

    init {
        codeAccount = savedStateHandle.get<Int>("code_account")
        codePaid = savedStateHandle.get<Int>("code_paid")
        main()
    }

    fun save(navController: NavController, context: Context) {
        validate()
        _paid?.let { paid ->
            paid.id.takeIf { it > 0 }?.let {
                if (paidSvc.update(paid)) {
                    Toast.makeText(context, R.string.toast_update_successful, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, R.string.toast_update_error, Toast.LENGTH_LONG).show()
                }
            } ?: run {
                val response = paidSvc.create(paid)
                if (response > 0) {
                    Toast.makeText(context, R.string.toast_save_successful, Toast.LENGTH_LONG).show()
                    _paid = _paid!!.copy(id = response)
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, R.string.toast_save_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun clean() {
        account.value = codeAccount?.let { accountList?.first { it.id == codeAccount } }
        date.value = null
        name.value = ""
        value.value = ""
        recurrent.value = false
        _paid = null
    }

    fun validate() {
        errorAccount.value = account.value?.takeIf { it.id > 0 } == null
        errorDate.value = date.value == null
        errorName.value = name.value.isEmpty()
        errorValue.value = value.value.isBlank() || NumbersUtil.toDouble(value.value) <= 0.0
        if (!errorAccount.value && !errorDate.value && !errorName.value && !errorValue.value) {
            _paid = PaidDTO(
                id = codePaid ?: 0,
                datePaid = LocalDateTime.of(date.value, LocalTime.MIN),
                itemName = name.value,
                itemValue = NumbersUtil.toDouble(value.value),
                recurrent = recurrent.value,
                account = account.value!!.id,
                end = LocalDateTime.now()
            )
        }
    }

    private fun main() = runBlocking {
        progressStatus.value = 0.0f
        execution()
        progressStatus.value = 1.0f
    }

    private suspend fun execution() {
        progressStatus.value = 0.2f
        date.value = LocalDate.now()

        accountSvc.getAllActive().takeIf { it.isNotEmpty() }?.let { list ->
            accountListPair.clear()
            accountList = list
            list.map {
                Pair(it.id, it.name)
            }.forEach(accountListPair::add)
            progressStatus.value = 0.4f
        }
        codePaid?.let {
            paidSvc.get(it)?.let {
                _paid = it
                date.value = it.datePaid.toLocalDate()
                name.value = it.itemName
                value.value = NumbersUtil.toString(it.itemValue)
                recurrent.value = it.recurrent
                progressStatus.value = 0.6f
            }
        }
        codeAccount?.let {
            Log.d("codeAccount", ">>> $it $accountList")
            account.value = accountList?.firstOrNull { acc -> acc.id == it }
            progressStatus.value = 0.8f
        }
        loading.value = false
    }

    companion object {
        fun create(extras: CreationExtras, accountSvc: IAccountPort, paidSvc: IPaidPort): PaidViewModel {
            val savedStateHandle = extras.createSavedStateHandle()
            return PaidViewModel(savedStateHandle, accountSvc, paidSvc)
        }
    }
}