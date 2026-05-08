package co.com.japl.module.creditcard.controllers.emailcreditcard.list

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.EmailCreditCard
import kotlinx.coroutines.runBlocking

class EmailCreditCardViewModel constructor(
    private val svc: IEmailCreditCardPort?,
    private val creditCardSvc: ICreditCardPort?,
    private val navController: NavController?
) : ViewModel() {

    val load = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val list = mutableStateListOf<Map<Int, List<EmailCreditCardDTO>>>()

    fun edit(code: Int) {
        require(code > 0) { "The code must be greater than 0" }
        navController?.let {
            EmailCreditCard.navigate(code, it)
        }
    }

    fun add() {
        navController?.let { EmailCreditCard.navigate(it) }
    }

    fun enabled(code: Int) {
        require(code > 0) { "The code must be greater than 0" }
        svc?.enable(code).takeIf { it == true }?.let {
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT).show() }?.also {
                load.value = true
            }
        } ?: navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT).show() }
    }

    fun duplicate(code: Int) {
        require(code > 0) { "The code must be greater than 0" }
        svc?.getById(code)?.let {
            svc?.create(it.copy(id = 0))?.let {
                progress.floatValue = 0f
                load.value = true
            }
        }
    }

    fun disabled(code: Int) {
        require(code > 0) { "The code must be greater than 0" }
        svc?.disable(code).takeIf { it == true }?.let {
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_disabled, Toast.LENGTH_SHORT).show() }?.also {
                load.value = true
            }
        } ?: navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_disabled, Toast.LENGTH_SHORT).show() }
    }

    fun delete(code: Int) {
        require(code > 0) { "The code must be greater than 0" }
        svc?.delete(code)?.takeIf { it }?.let {
            navController?.let {
                Toast.makeText(it.context, R.string.toast_successful_deleted, Toast.LENGTH_SHORT).show().also {
                    // navController.popBackStack() // In the list we don't want to pop back stack usually, but follow SMS implementation
                    load.value = true
                }
            }
        } ?: navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_deleted, Toast.LENGTH_SHORT).show() }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    suspend fun execute() {
        svc?.let {
            creditCardSvc?.let {
                it.getAll().takeIf { it.isNotEmpty() }?.map { dto ->
                    val list = svc.getAllByCodeCreditCard(dto.id)
                    list.map { it.copy(nameCreditCard = dto.name) }
                }?.flatten()?.groupBy { it.codeCreditCard }?.let {
                    it.map {
                        mapOf(it.key to it.value)
                    }?.let {
                        list.clear()
                        list.addAll(it)
                    }
                }?.also { progress.floatValue = 0.5f }
            }
        }
        load.value = false
    }
}
