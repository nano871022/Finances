package co.com.japl.module.paid.controllers.sms.list

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.navigations.Sms
import kotlinx.coroutines.runBlocking

class SmsViewModel constructor(private val svc: ISMSPaidPort?, private val accountSvc:IAccountPort?, private val navController: NavController?): ViewModel() {

    val  load = mutableStateOf(true)
    val  progress = mutableFloatStateOf(0.0f)

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
        svc?.enable(code).takeIf { it == true }?.let {
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT).show() }?.also {
                load.value = true
            }
        }?:navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT).show() }
    }

    fun duplicate(code:Int){
        require(code > 0){"The code must be greater than 0"}
        svc?.getById(code)?.let{
            svc?.create(it.copy(id=0))?.let{
                progress.floatValue = 0f
                load.value = true
            }
        }
    }

    fun disabled(code:Int){
        require(code > 0){"The code must be greater than 0"}
        svc?.disable(code).takeIf { it == true }?.let {
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_disabled, Toast.LENGTH_SHORT).show() }?.also {
                load.value = true
            }
        }?:navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_disabled, Toast.LENGTH_SHORT).show() }
    }

    fun delete(code:Int){
        require(code > 0){"The code must be greater than 0"}
        svc?.delete(code)?.takeIf { it }?.let{
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_deleted, Toast.LENGTH_SHORT).show().also {
                navController.popBackStack()
            }}
        }?:navController?.let { Toast.makeText(it.context, R.string.toast_unsuccessful_deleted, Toast.LENGTH_SHORT).show() }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    suspend fun execute() {
        svc?.let {
            accountSvc?.let {
                it.getAll().takeIf { it.isNotEmpty() }?.map{ dto->
                    val list = svc.getAllByCodeAccount(dto.id)
                    list.map { it.copy(nameAccount = dto.name) }
                }?.flatten()?.groupBy{it.codeAccount}?.let{
                        it.map {
                            mapOf(it.key to it.value)
                        }?.let{
                            list.clear()
                            list.addAll(it)
                        }

                    }.also { progress.floatValue = 0.5f }
            }
        }
        load.value = false
    }


}