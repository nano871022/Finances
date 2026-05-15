package co.com.japl.module.creditcard.controllers.emailcreditcard.form

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EmailCreditCardViewModel constructor(
    private val codeEmailCC: Int?,
    private val svc: IEmailCreditCardPort?,
    private val creditCardSvc: ICreditCardPort?,
    private val navController: NavController?
) : ViewModel() {

    val load = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    private var creditCardLists = mutableListOf<CreditCardDTO>()
    private var emailCreditCard: EmailCreditCardDTO? = null

    val creditCardList = mutableStateListOf<Pair<Int, String>>()
    val kindInterestRateList = mutableStateListOf<Pair<Int, String>>()

    var creditCard = mutableStateOf<Pair<Int, String>?>(null)
    val errorCreditCard = mutableStateOf(false)
    val kindInterestRate = mutableStateOf<Pair<Int, String>?>(null)
    val errorKindInterestRate = mutableStateOf(false)
    val sender = mutableStateOf("")
    val errorSender = mutableStateOf(false)
    val subjectPattern = mutableStateOf("")
    val errorSubjectPattern = mutableStateOf(false)
    val bodyPattern = mutableStateOf("")
    val errorBodyPattern = mutableStateOf(false)
    val validationResult = mutableStateOf("")

    fun save() {
        validate()
        if (emailCreditCard != null && !errorKindInterestRate.value && !errorSender.value && !errorSubjectPattern.value && !errorBodyPattern.value && !errorCreditCard.value) {
            emailCreditCard?.takeIf { it.id == 0 }?.let {
                svc?.create(it)?.takeIf { it > 0 }?.let {
                    emailCreditCard = emailCreditCard!!.copy(id = it)
                    navController?.let {
                        Toast.makeText(it.context, R.string.toast_successful_insert, Toast.LENGTH_SHORT).show().also {
                            navController.popBackStack()
                        }
                    }
                } ?: navController?.let { Toast.makeText(it.context, R.string.toast_unsuccessful_insert, Toast.LENGTH_SHORT).show() }
            }
            emailCreditCard?.takeIf { it.id > 0 }?.let {
                svc?.update(it)?.takeIf { it }?.let {
                    navController?.let {
                        Toast.makeText(it.context, R.string.toast_successful_update, Toast.LENGTH_SHORT).show().also {
                            navController.popBackStack()
                        }
                    }
                } ?: navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_update, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    fun clean() {
        kindInterestRate.value = null
        sender.value = ""
        subjectPattern.value = ""
        bodyPattern.value = ""
        creditCard.value = null
        emailCreditCard = null
        progress.floatValue = 0.0f
        load.value = true
        errorKindInterestRate.value = false
        errorSender.value = false
        errorSubjectPattern.value = false
        errorBodyPattern.value = false
        errorCreditCard.value = false
    }

    fun validatePatternWithMessages() {
        emailCreditCard?.let { dto ->
            svc?.let { port ->
                validationResult.value = ""
                viewModelScope.launch {
                    runCatching {
                        port.validateMessagePattern(dto)
                    }.onFailure {
                        validationResult.value = "Error: ${it.message}"
                    }.onSuccess { list ->
                        if (list.isNotEmpty()) {
                            validationResult.value = list.joinToString("\n\n")
                        } else {
                            validationResult.value = "Not found messages"
                        }
                    }
                }
            }
        }
    }

    fun validate() {
        kindInterestRate.value.takeIf { it != null }?.let {
            errorKindInterestRate.value = false
        } ?: errorKindInterestRate.let { it.value = true }

        sender.value.takeIf { it.isNotEmpty() }?.let {
            errorSender.value = false
        } ?: errorSender.let { it.value = true }

        subjectPattern.value.takeIf { it.isNotEmpty() }?.let {
            errorSubjectPattern.value = false
        } ?: errorSubjectPattern.let { it.value = true }

        bodyPattern.value.takeIf { it.isNotEmpty() }?.let {
            errorBodyPattern.value = false
        } ?: errorBodyPattern.let { it.value = true }

        creditCard.value?.let {
            errorCreditCard.value = false
        } ?: errorCreditCard.let { it.value = true }

        if (!errorKindInterestRate.value && !errorSender.value && !errorSubjectPattern.value && !errorBodyPattern.value && !errorCreditCard.value) {
            emailCreditCard = EmailCreditCardDTO(
                id = emailCreditCard?.id ?: 0,
                sender = sender.value,
                subjectPattern = subjectPattern.value,
                bodyPattern = bodyPattern.value,
                kindInterestRateEnum = KindInterestRateEnum.valueOf(kindInterestRate.value?.second ?: ""),
                codeCreditCard = creditCard.value?.first ?: 0,
                nameCreditCard = creditCard.value?.second ?: "",
                active = true
            )
        }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    suspend fun execute() {
        svc?.let {
            codeEmailCC?.let {
                svc.getById(it)?.let {
                    emailCreditCard = it
                    kindInterestRate.value = it.kindInterestRateEnum.let {
                        Pair(it.getCode().toInt(), it.name)
                    }
                    sender.value = it.sender
                    subjectPattern.value = it.subjectPattern
                    bodyPattern.value = it.bodyPattern
                    progress.floatValue = 0.3f
                }
            }
        }
        creditCardSvc?.let {
            creditCardList.clear()
            creditCardLists.clear()
            it.getAll()?.takeIf { it.isNotEmpty() }?.forEach {
                creditCardLists.add(it)
                creditCardList.add(Pair(it.id, it.name))
            }?.also { progress.floatValue = 0.6f }
            emailCreditCard?.let { email ->
                creditCardLists.firstOrNull { it.id == email.codeCreditCard }?.let { creditCard.value = Pair(it.id, it.name) }
            }
        }

        kindInterestRateList.clear()
        KindInterestRateEnum.values().map { Pair(it.getCode().toInt(), it.name) }.forEach(kindInterestRateList::add)
            .also { progress.floatValue = 0.8f }

        load.value = false
    }
}
