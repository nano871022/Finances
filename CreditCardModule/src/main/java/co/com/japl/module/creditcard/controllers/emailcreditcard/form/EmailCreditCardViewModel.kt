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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        emailCreditCard?.let { dto ->
            if (!errorKindInterestRate.value && !errorSender.value && !errorSubjectPattern.value && !errorBodyPattern.value && !errorCreditCard.value) {
                viewModelScope.launch(Dispatchers.IO) {
                    if (dto.id == 0) {
                        val id = svc?.create(dto) ?: 0
                        if (id > 0) {
                            emailCreditCard = dto.copy(id = id)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(navController?.context, R.string.toast_successful_insert, Toast.LENGTH_SHORT).show()
                                navController?.popBackStack()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(navController?.context, R.string.toast_unsuccessful_insert, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        val result = svc?.update(dto) ?: false
                        if (result) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(navController?.context, R.string.toast_successful_update, Toast.LENGTH_SHORT).show()
                                navController?.popBackStack()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(navController?.context, R.string.toast_dont_successful_update, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
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
        validationResult.value = ""
    }

    fun validatePatternWithMessages() {
        validate()
        emailCreditCard?.let { dto ->
            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    svc?.validateMessagePattern(dto) ?: emptyList()
                }.onFailure { e ->
                    withContext(Dispatchers.Main) {
                        validationResult.value = "Error: ${e.message}"
                    }
                }.onSuccess { list ->
                    withContext(Dispatchers.Main) {
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
        errorKindInterestRate.value = kindInterestRate.value == null
        errorSender.value = sender.value.isEmpty()
        errorSubjectPattern.value = subjectPattern.value.isEmpty()
        errorBodyPattern.value = bodyPattern.value.isEmpty()
        errorCreditCard.value = creditCard.value == null

        if (!errorKindInterestRate.value && !errorSender.value && !errorSubjectPattern.value && !errorBodyPattern.value && !errorCreditCard.value) {
            emailCreditCard = EmailCreditCardDTO(
                id = emailCreditCard?.id ?: 0,
                sender = sender.value,
                subjectPattern = subjectPattern.value,
                bodyPattern = bodyPattern.value,
                kindInterestRateEnum = KindInterestRateEnum.entries.firstOrNull { it.name == kindInterestRate.value?.second } ?: KindInterestRateEnum.CREDIT_CARD,
                codeCreditCard = creditCard.value?.first ?: 0,
                nameCreditCard = creditCard.value?.second ?: "",
                active = true
            )
        }
    }

    fun main() {
        viewModelScope.launch(Dispatchers.IO) {
            progress.floatValue = 0.1f
            execute()
            withContext(Dispatchers.Main) {
                progress.floatValue = 1.0f
                load.value = false
            }
        }
    }

    private suspend fun execute() {
        svc?.let { port ->
            codeEmailCC?.takeIf { it > 0 }?.let { id ->
                port.getById(id)?.let { dto ->
                    emailCreditCard = dto
                    withContext(Dispatchers.Main) {
                        kindInterestRate.value = dto.kindInterestRateEnum.let { kind ->
                            Pair(kind.getCode().toInt(), kind.name)
                        }
                        sender.value = dto.sender
                        subjectPattern.value = dto.subjectPattern
                        bodyPattern.value = dto.bodyPattern
                    }
                    progress.floatValue = 0.3f
                }
            }
        }
        creditCardSvc?.let { port ->
            val list = port.getAll()
            withContext(Dispatchers.Main) {
                creditCardList.clear()
                creditCardLists.clear()
                list.forEach { cc ->
                    creditCardLists.add(cc)
                    creditCardList.add(Pair(cc.id, cc.name))
                }
                emailCreditCard?.let { email ->
                    creditCardLists.firstOrNull { it.id == email.codeCreditCard }?.let { cc ->
                        creditCard.value = Pair(cc.id, cc.name)
                    }
                }
            }
            progress.floatValue = 0.6f
        }

        withContext(Dispatchers.Main) {
            kindInterestRateList.clear()
            KindInterestRateEnum.entries.map { Pair(it.getCode().toInt(), it.name) }.forEach { kindInterestRateList.add(it) }
        }
        progress.floatValue = 0.8f
    }
}
