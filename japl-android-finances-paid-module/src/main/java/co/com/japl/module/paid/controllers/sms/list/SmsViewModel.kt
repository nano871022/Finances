package co.com.japl.module.paid.controllers.sms.list

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.navigations.Sms
import kotlinx.coroutines.runBlocking

class SmsViewModel constructor(
    private val svc: ISMSPaidPort,
    private val accountSvc: IAccountPort
) : ViewModel() {

    val load = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val list = mutableStateListOf<Map<Int, List<SMSPaidDTO>>>()

    fun edit(code: Int, navController: NavController) {
        require(code > 0) { "The code must be greater than 0" }
        Sms.navigate(code, navController)
    }

    fun add(navController: NavController) {
        Sms.navigate(navController)
    }

    fun enabled(code: Int, context: Context) {
        require(code > 0) { "The code must be greater than 0" }
        if (svc.enable(code)) {
            Toast.makeText(context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT).show()
            load.value = true
        } else {
            Toast.makeText(context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT).show()
        }
    }

    fun disabled(code: Int, context: Context) {
        require(code > 0) { "The code must be greater than 0" }
        if (svc.disable(code)) {
            Toast.makeText(context, R.string.toast_successful_disabled, Toast.LENGTH_SHORT).show()
            load.value = true
        } else {
            Toast.makeText(context, R.string.toast_dont_successful_disabled, Toast.LENGTH_SHORT).show()
        }
    }

    fun delete(code: Int, context: Context, navController: NavController) {
        require(code > 0) { "The code must be greater than 0" }
        if (svc.delete(code)) {
            Toast.makeText(context, R.string.toast_successful_deleted, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else {
            Toast.makeText(context, R.string.toast_unsuccessful_deleted, Toast.LENGTH_SHORT).show()
        }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    private suspend fun execute() {
        accountSvc.getAll().takeIf { it.isNotEmpty() }?.map { dto ->
            val list = svc.getAllByCodeAccount(dto.id)
            list.map { it.copy(nameAccount = dto.name) }
        }?.flatten()?.groupBy { it.codeAccount }?.let {
            it.map {
                mapOf(it.key to it.value)
            }.let {
                list.clear()
                list.addAll(it)
            }
        }.also { progress.floatValue = 0.5f }
        load.value = false
    }

    companion object {
        fun create(
            extras: CreationExtras,
            smsSvc: ISMSPaidPort,
            accountSvc: IAccountPort
        ): SmsViewModel {
            return SmsViewModel(smsSvc, accountSvc)
        }
    }
}