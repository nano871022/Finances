package co.com.japl.module.creditcard.controllers.smscreditcard.list

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.module.creditcard.R
import kotlinx.coroutines.runBlocking

class SmsCreditCardViewModel constructor(private val svc:ISMSCreditCardPort?,private val creditCardSvc:ICreditCardPort?,private val savedStateHandle: SavedStateHandle?): ViewModel() {

    val  load = mutableStateOf(true)
    val  progress = mutableFloatStateOf(0.0f)

    val list = mutableStateListOf<Map<Int,List<SMSCreditCard>>>()

    fun edit(code:Int,navController: NavController){
        require(code > 0){"The code must be greater than 0"}
        co.com.japl.module.creditcard.navigations.SMSCreditCard.navigate(code,navController)
    }

    fun add(navController: NavController){
        co.com.japl.module.creditcard.navigations.SMSCreditCard.navigate(navController)
    }

    fun enabled(code:Int){
        require(code > 0){"The code must be greater than 0"}
        svc?.enable(code).takeIf { it == true }?.let {
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT).show() }?.also {
                load.value = true
            }
        }?:navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT).show() }
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
        }?:navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_deleted, Toast.LENGTH_SHORT).show() }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    suspend fun execute() {
        svc?.let {
            creditCardSvc?.let {
                it.getAll().takeIf { it.isNotEmpty() }?.map{ dto->
                    val list = svc.getAllByCodeCreditCard(dto.id)
                    list.map { it.copy(nameCreditCard = dto.name) }
                }?.flatten()?.groupBy{it.codeCreditCard}?.let{
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