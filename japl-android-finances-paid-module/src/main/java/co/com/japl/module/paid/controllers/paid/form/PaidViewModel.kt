package co.com.japl.module.paid.controllers.paid.form

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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

class PaidViewModel (
        private val savedStateHandle: SavedStateHandle,
        private val accountSvc: IAccountPort?,
        private val paidSvc: IPaidPort?) :ViewModel(){
    private var codeAccount:Int = 0
    private var codePaid:Int = 0
    private var _paid: PaidDTO? = null
    var accountList :List<AccountDTO>? = null
    val accountListPair = mutableStateListOf<Pair<Int,String>>()


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

    init{
        savedStateHandle.get<Int>("code_account")?.let{
            codeAccount = it
        }
        savedStateHandle.get<Int>("code_paid")?.let{
            codePaid = it
        }
    }
    fun save(navController: NavController){
        validate()
        _paid?.let{paid->
            paid.id.takeIf { it > 0 }?.let{
                paidSvc?.let {
                    if(it.update(paid)){
                        navController?.let { Toast.makeText(navController.context, R.string.toast_update_successful,Toast.LENGTH_LONG).show().also {
                            navController.popBackStack()
                        }}
                    }else{
                        navController?.let { Toast.makeText(navController.context, R.string.toast_update_error,Toast.LENGTH_LONG).show() }
                    }
                }
            }?:paidSvc?.let{svc->
                val response = svc.create(paid)
                if(response > 0){
                    navController?.let { Toast.makeText(navController.context, R.string.toast_save_successful,Toast.LENGTH_LONG).show().also {
                        navController.popBackStack()
                        _paid = _paid!!.copy(id = response)

                    }}
                }else{
                    navController?.let { Toast.makeText(navController.context, R.string.toast_save_error,Toast.LENGTH_LONG).show() }
                }
            }
        }
    }

    fun clean(){
        account.value = codeAccount?.let{ accountList?.first { it.id == codeAccount } }
        date.value = null
        name.value = ""
        value.value = ""
        recurrent.value = false

        _paid = null
    }

    fun validate(){
        account.value?.takeIf { it.id > 0 }?.let {
            errorAccount.value = false
        }?:errorAccount.let{it.value = true}

        date.value?.let {
            errorDate.value = false
        }?:errorDate.let{it.value = true}

        name.value.takeIf { it.isNotEmpty() }?.let {
            errorName.value = false
        }?:errorName.let{it.value = true}

        value.value.takeIf { it.isNotBlank() && NumbersUtil.toDouble(it) > 0.0 }?.let {
            errorValue.value = false
        }?:errorValue.let{it.value = true}
        if(!errorAccount.value && !errorDate.value && !errorName.value && !errorValue.value){
            _paid = PaidDTO(id = codePaid?:0,
                datePaid = LocalDateTime.of(date.value, LocalTime.MIN),
                itemName = name.value,
                itemValue = NumbersUtil.toDouble(value.value),
                recurrent = recurrent.value,
                account = account.value!!.id,
                end = LocalDateTime.now())
        }
    }

    fun main()= runBlocking {
        progressStatus.value = 0.0f
        execution()
        progressStatus.value = 1.0f
    }

    suspend fun execution(){
        progressStatus.value = 0.2f
        date.value = LocalDate.now()

        accountSvc?.let {
            it.getAllActive().takeIf { it.isNotEmpty() }?.let { list ->
                accountListPair.clear()
                accountList= list
                list.map{
                    Pair(it.id,it.name)
                }.forEach (accountListPair::add)
                progressStatus.value = 0.4f
            }
        }
        codePaid?.let{
            paidSvc?.let{
                it.get(codePaid)?.let{
                    _paid = it
                    date.value = it.datePaid.toLocalDate()
                    name.value = it.itemName
                    value.value = NumbersUtil.toString(it.itemValue)
                    recurrent.value = it.recurrent
                    progressStatus.value = 0.6f
                }

            }
        }
        codeAccount?.let{
            Log.d("codeAccount",">>> $it $accountList")
            account.value = accountList?.firstOrNull { it.id == codeAccount }
            progressStatus.value = 0.8f
        }
        loading.value = false
    }

}