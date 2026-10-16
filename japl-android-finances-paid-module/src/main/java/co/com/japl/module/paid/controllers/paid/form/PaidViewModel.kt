package co.com.japl.module.paid.controllers.paid.form

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.R
import co.com.japl.ui.utils.NumbersUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel(assistedFactory = PaidViewModel.Factory::class)
class PaidViewModel @AssistedInject constructor(
    @Assisted("codeAccount") private val codeAccount: Int?,
    @Assisted("codePaid") private val codePaid: Int?,
    private val accountSvc: IAccountPort?,
    private val paidSvc: IPaidPort?,
    @Assisted private val navController: NavController?
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("codeAccount") codeAccount: Int?,
            @Assisted("codePaid") codePaid: Int?,
            @Assisted navController: NavController?
        ): PaidViewModel
    }

    private var _paid: PaidDTO? = null
    var accountList: List<AccountDTO>? = null
    val accountListPair = mutableStateListOf<Pair<Int, String>>()

    val loading = mutableStateOf(true)
    val progressStatus = mutableFloatStateOf(0.0f)

    val account = mutableStateOf<AccountDTO?>(null)
    val errorAccount = mutableStateOf(false)
    val date = mutableStateOf<LocalDate?>(null)
    val errorDate = mutableStateOf(false)
    val name = mutableStateOf("")
    val errorName = mutableStateOf(false)
    val value = mutableStateOf("")
    val errorValue = mutableStateOf(false)
    val recurrent = mutableStateOf(false)

    fun save() {
        validate()
        _paid?.let { paid ->
            viewModelScope.launch {
                try {
                    if (paid.id > 0) {
                        paidSvc?.let { svc ->
                            val success = withContext(Dispatchers.IO) { svc.update(paid) }
                            if (success) {
                                navController?.let { nav ->
                                    Toast.makeText(nav.context, R.string.toast_update_successful, Toast.LENGTH_LONG).show()
                                    nav.popBackStack()
                                }
                            } else {
                                navController?.let { nav ->
                                    Toast.makeText(nav.context, R.string.toast_update_error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        paidSvc?.let { svc ->
                            val response = withContext(Dispatchers.IO) { svc.create(paid) }
                            if (response > 0) {
                                navController?.let { nav ->
                                    Toast.makeText(nav.context, R.string.toast_save_successful, Toast.LENGTH_LONG).show()
                                    _paid = _paid!!.copy(id = response)
                                    nav.popBackStack()
                                }
                            } else {
                                navController?.let { nav ->
                                    Toast.makeText(nav.context, R.string.toast_save_error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(javaClass.name, "Error saving payment: ${e.message}", e)
                    navController?.let { nav ->
                        Toast.makeText(nav.context, R.string.toast_save_error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun clean() {
        account.value = codeAccount?.let { accCode -> accountList?.firstOrNull { it.id == accCode } }
        date.value = null
        name.value = ""
        value.value = ""
        recurrent.value = false

        _paid = null
    }

    fun validate() {
        account.value?.takeIf { it.id > 0 }?.let {
            errorAccount.value = false
        } ?: errorAccount.let { it.value = true }

        date.value?.let {
            errorDate.value = false
        } ?: errorDate.let { it.value = true }

        name.value.takeIf { it.isNotEmpty() }?.let {
            errorName.value = false
        } ?: errorName.let { it.value = true }

        value.value.takeIf { it.isNotBlank() && NumbersUtil.toDouble(it) > 0.0 }?.let {
            errorValue.value = false
        } ?: errorValue.let { it.value = true }

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

    fun main() {
        progressStatus.value = 0.0f
        loading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    execution()
                }
            } catch (e: Exception) {
                Log.e(javaClass.name, "Error in execution: ${e.message}", e)
            } finally {
                progressStatus.value = 1.0f
                loading.value = false
            }
        }
    }

    suspend fun execution() {
        try {
            progressStatus.value = 0.2f
            date.value = LocalDate.now()

            accountSvc?.let {
                it.getAllActive().takeIf { list -> list.isNotEmpty() }?.let { list ->
                    accountListPair.clear()
                    accountList = list
                    list.map { acc ->
                        Pair(acc.id, acc.name)
                    }.forEach(accountListPair::add)
                    progressStatus.value = 0.4f
                }
            }
            codePaid?.let { codeP ->
                paidSvc?.let { svc ->
                    svc.get(codeP)?.let { dto ->
                        _paid = dto
                        date.value = dto.datePaid.toLocalDate()
                        name.value = dto.itemName
                        value.value = NumbersUtil.toString(dto.itemValue)
                        recurrent.value = dto.recurrent
                        progressStatus.value = 0.6f
                    }
                }
            }
            codeAccount?.let { accCode ->
                // debug log
                account.value = accountList?.firstOrNull { it.id == accCode }
                progressStatus.value = 0.8f
            }
        } finally {
            loading.value = false
        }
    }
}
