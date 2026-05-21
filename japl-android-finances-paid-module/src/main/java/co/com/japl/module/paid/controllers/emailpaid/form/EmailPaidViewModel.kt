package co.com.japl.module.paid.controllers.emailpaid.form

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.module.paid.R
import co.com.japl.ui.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailPaidViewModel(
    private val codeEmailPaid: Int?,
    private val svc: IEmailPaidPort?,
    private val accountSvc: IAccountPort?,
    private val navController: NavController?,
    private val llmService: ILLMService? = null,
    private val prefs: Prefs? = null,
    private val context: Context? = null
) : ViewModel() {

    val load = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    private var emailPaid: EmailPaidDTO? = null

    val accountList = mutableStateListOf<Pair<Int, String>>()
    var account = mutableStateOf<Pair<Int, String>?>(null)
    val errorAccount = mutableStateOf(false)
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
    val llmModels = mutableStateListOf<Pair<Int, String>>()
    val selectedLLMModel = mutableStateOf<Pair<Int, String>?>(null)

    fun loadEmailSamples() {
        if (sender.value.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                svc?.getEmailList(sender.value, subjectPattern.value, 30)?.let { list ->
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
                llmService?.getAiResponse(prompt, selectedLLMModel.value?.second)?.onSuccess { response ->
                    withContext(Dispatchers.Main) {
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
                        load.value = false
                        showEmailDialog.value = false
                        context?.let { Toast.makeText(it, R.string.ai_response, Toast.LENGTH_SHORT).show() }
                    }
                }?.onFailure {
                    withContext(Dispatchers.Main) {
                        aiFailed.value = true
                        load.value = false
                        showEmailDialog.value = false
                    }
                }
            }
        }
    }

    fun isAIValid(): Boolean = prefs?.llmEnabled ?: false

    fun save() {
        validate()
        emailPaid?.let { dto ->
            if (!errorAccount.value && !errorSender.value && !errorSubjectPattern.value && !errorBodyPattern.value) {
                viewModelScope.launch(Dispatchers.IO) {
                    if (dto.id == 0) {
                        val id = svc?.create(dto) ?: 0
                        if (id > 0) {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_save_successful, Toast.LENGTH_SHORT).show() }
                                navController?.popBackStack()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_save_error, Toast.LENGTH_SHORT).show() }
                            }
                        }
                    } else {
                        if (svc?.update(dto) == true) {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_update_successful, Toast.LENGTH_SHORT).show() }
                                navController?.popBackStack()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                context?.let { Toast.makeText(it, R.string.toast_update_error, Toast.LENGTH_SHORT).show() }
                            }
                        }
                    }
                }
            }
        }
    }

    fun clean() {
        sender.value = ""
        subjectPattern.value = ""
        bodyPattern.value = ""
        account.value = null
        errorAccount.value = false
        errorSender.value = false
        errorSubjectPattern.value = false
        errorBodyPattern.value = false
        validationResults.clear()
    }

    fun validatePatternWithMessages() {
        validate()
        emailPaid?.let { dto ->
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    validating.value = true
                    showPopup.value = true
                    validationResults.clear()
                }
                svc?.validateMessagePattern(dto, 30)?.let { list ->
                    withContext(Dispatchers.Main) {
                        validating.value = false
                        validationResults.addAll(list)
                        validationResult.value = if (list.isNotEmpty()) "Success" else "Not found messages"
                    }
                }
            }
        }
    }

    fun validate() {
        errorAccount.value = account.value == null
        errorSender.value = sender.value.isEmpty()
        errorSubjectPattern.value = subjectPattern.value.isEmpty()
        errorBodyPattern.value = bodyPattern.value.isEmpty()

        if (!errorAccount.value && !errorSender.value && !errorSubjectPattern.value && !errorBodyPattern.value) {
            emailPaid = EmailPaidDTO(
                id = emailPaid?.id ?: 0,
                sender = sender.value,
                subjectPattern = subjectPattern.value,
                bodyPattern = bodyPattern.value,
                codeAccount = account.value?.first ?: 0,
                nameAccount = account.value?.second ?: "",
                active = true
            )
        }
    }

    fun main() {
        viewModelScope.launch(Dispatchers.IO) {
            accountSvc?.getAll()?.forEach { accountList.add(Pair(it.id, it.name)) }
            codeEmailPaid?.takeIf { it > 0 }?.let { id ->
                svc?.getById(id)?.let { dto ->
                    emailPaid = dto
                    withContext(Dispatchers.Main) {
                        sender.value = dto.sender
                        subjectPattern.value = dto.subjectPattern
                        bodyPattern.value = dto.bodyPattern
                        account.value = Pair(dto.codeAccount, dto.nameAccount)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                llmService?.getModels()?.onSuccess { models ->
                    llmModels.clear()
                    models.forEachIndexed { index, name ->
                        llmModels.add(Pair(index, name))
                    }
                    val model = prefs?.llmModel ?: ""
                    selectedLLMModel.value = llmModels.firstOrNull { it.second == model } ?: llmModels.firstOrNull()
                }

                load.value = false
            }
        }
    }
}
