package co.com.japl.module.paid.controllers.sms.list

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.navigations.Sms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsViewModel(private val svc: ISMSPaidPort?, private val accountSvc: IAccountPort?, private val navController: NavController?): ViewModel() {

    val load = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val list = mutableStateListOf<Map<Int,List<SMSPaidDTO>>>()

    fun edit(code:Int){
        require(code > 0){"The code must be greater than 0"}
        navController?.let{
            Sms.navigate(code,navController)
        }
    }

    fun add(){
        navController?.let{Sms.navigate(navController)}
    }

    fun enabled(code:Int){
        require(code > 0){"The code must be greater than 0"}
        viewModelScope.launch(Dispatchers.IO) {
            val result = svc?.enable(code) == true
            withContext(Dispatchers.Main) {
                if (result) {
                    navController?.let { Toast.makeText(it.context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT).show() }
                    load.value = true
                } else {
                    navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    fun duplicate(code:Int){
        require(code > 0){"The code must be greater than 0"}
        viewModelScope.launch(Dispatchers.IO) {
            val item = svc?.getById(code)
            if (item != null) {
                svc?.create(item.copy(id = 0))
                withContext(Dispatchers.Main) {
                    progress.floatValue = 0f
                    load.value = true
                }
            }
        }
    }

    fun disabled(code:Int){
        require(code > 0){"The code must be greater than 0"}
        viewModelScope.launch(Dispatchers.IO) {
            val result = svc?.disable(code) == true
            withContext(Dispatchers.Main) {
                if (result) {
                    navController?.let { Toast.makeText(it.context, R.string.toast_successful_disabled, Toast.LENGTH_SHORT).show() }
                    load.value = true
                } else {
                    navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_disabled, Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    fun delete(code:Int){
        require(code > 0){"The code must be greater than 0"}
        viewModelScope.launch(Dispatchers.IO) {
            val result = svc?.delete(code) == true
            withContext(Dispatchers.Main) {
                if (result) {
                    navController?.let { Toast.makeText(it.context, R.string.toast_successful_deleted, Toast.LENGTH_SHORT).show() }
                    load.value = true
                } else {
                    navController?.let { Toast.makeText(it.context, R.string.toast_unsuccessful_deleted, Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    fun execute() = viewModelScope.launch {
        progress.floatValue = 0.1f
        svc?.let {
            accountSvc?.let { accSvc ->
                val accounts = withContext(Dispatchers.IO) { accSvc.getAll() }
                if (accounts.isNotEmpty()) {
                    val grouped = accounts.mapNotNull { dto ->
                        val items = withContext(Dispatchers.IO) { svc.getAllByCodeAccount(dto.id) }
                        if (items.isNotEmpty()) {
                            items.map { it.copy(nameAccount = dto.name) }
                        } else null
                    }.flatten().groupBy { it.codeAccount }.map { mapOf(it.key to it.value) }

                    list.clear()
                    list.addAll(grouped)
                } else {
                    list.clear()
                }
            }
        }
        progress.floatValue = 1.0f
        load.value = false
    }
}
