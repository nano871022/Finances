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
import android.util.Log
import android.content.Context
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailCreditCardViewModel constructor(
    private val codeEmailCC: Int?,
    private val svc: IEmailCreditCardPort?,
    private val creditCardSvc: ICreditCardPort?,
    private val navController: NavController?,
    private val llmService: ILLMService? = null,
    private val prefs: Prefs? = null,
    private val context: Context? = null
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
    val showPopup = mutableStateOf(false)
    val validating = mutableStateOf(false)
    val validationResults = mutableStateListOf<EmailValidationDTO>()

    val aiFailed = mutableStateOf(false)
    val emailSamples = mutableStateListOf<Pair<String, Boolean>>()
    val showEmailDialog = mutableStateOf(false)

    fun loadEmailSamples() {
        if (sender.value.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                svc?.getEmailList(sender.value, subjectPattern.value)?.let { list ->
                    withContext(Dispatchers.Main) {
                        emailSamples.clear()
                        list.map { Pair(it, false) }.forEach(emailSamples::add)
                        showEmailDialog.value = true
                    }
                }
            }
        }
    }

    fun generateRegexWithAI() {
        val selectedEmails = emailSamples.filter { it.second }.map { it.first }
        if (selectedEmails.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    load.value = true
                    progress.floatValue = 0.1f
                }
                val prompt = context?.getString(R.string.promt_email_expreg, selectedEmails.joinToString("\n")) ?: ""
                withContext(Dispatchers.Main) {
                    progress.floatValue = 0.3f
                }
                llmService?.getAiResponse(prompt)?.onSuccess { response ->
                    withContext(Dispatchers.Main) {
                        progress.floatValue = 0.6f
                        val lines = response.trim().lines()
                        lines.forEach { line ->
                            if (line.startsWith("SUBJECT:", ignoreCase = true)) {
                                subjectPattern.value = line.substringAfter("SUBJECT:").trim()
                            } else if (line.startsWith("BODY:", ignoreCase = true)) {
                                bodyPattern.value = line.substringAfter("BODY:").trim().removeSurrounding("`").removePrefix("regex").trim()
                            }
                        }
                        if (lines.size == 1 && !response.contains("SUBJECT:", ignoreCase = true)) {
                             bodyPattern.value = response.trim().removeSurrounding("`").removePrefix("regex").trim()
                        }
                        progress.floatValue = 1.0f
                        load.value = false
                        showEmailDialog.value = false
                        context?.let { Toast.makeText(it, R.string.ai_response, Toast.LENGTH_SHORT).show() }
                    }
                }?.onFailure {
                    Log.e("EmailCCViewModel", "Error: ${it.message}")
                    withContext(Dispatchers.Main) {
                        aiFailed.value = true
                        load.value = false
                        showEmailDialog.value = false
                        progress.floatValue = 1.0f
                    }
                }
            }
        }
    }

    fun isAIValid(): Boolean {
        return prefs?.llmEnabled ?: false
    }

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
                                context?.let { Toast.makeText(it, R.string.toast_successful_insert, Toast.LENGTH_SHORT).show() }
                                navController?.popBackStack()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_unsuccessful_insert, Toast.LENGTH_SHORT).show() }
                            }
                        }
                    } else {
                        val result = svc?.update(dto) ?: false
                        if (result) {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_successful_update, Toast.LENGTH_SHORT).show() }
                                navController?.popBackStack()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_dont_successful_update, Toast.LENGTH_SHORT).show() }
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
        showPopup.value = false
        validating.value = false
        validationResults.clear()
    }

    fun validatePatternWithMessages() {
        validate()
        emailCreditCard?.let { dto ->
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    validating.value = true
                    showPopup.value = true
                    validationResults.clear()
                }
                runCatching {
                    svc?.validateMessagePattern(dto) ?: emptyList()
                }.onFailure { e ->
                    withContext(Dispatchers.Main) {
                        validating.value = false
                        validationResult.value = "Error: ${e.message}"
                    }
                }.onSuccess { list ->
                    withContext(Dispatchers.Main) {
                        validating.value = false
                        validationResults.addAll(list)
                        if (list.isNotEmpty()) {
                            validationResult.value = "Success"
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
