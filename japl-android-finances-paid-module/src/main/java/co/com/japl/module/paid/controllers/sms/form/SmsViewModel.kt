package co.com.japl.module.paid.controllers.sms.form

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.module.paid.R
import co.com.japl.ui.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SmsViewModel constructor(private val codeSMS:Int?, private val svc:ISMSPaidPort?, private val accountSvc:IAccountPort?, private val navController: NavController?, private val llmService: ILLMService? = null, val prefs: Prefs?=null): ViewModel() {

    val  load = mutableStateOf(true)
    val  progress = mutableFloatStateOf(0.0f)

    private var accountLists = mutableListOf<AccountDTO>()
   private var smsPaid:SMSPaidDTO? = null

    val accountList = mutableStateListOf<Pair<Int,String>>()
    val kindInterestRateList = mutableStateListOf<Pair<Int,String>>()

    var account = mutableStateOf<Pair<Int,String>?>(null)
    val errorAccount = mutableStateOf(false)
    val phoneNumber = mutableStateOf("")
    val errorPhoneNumber = mutableStateOf(false)
    val pattern = mutableStateOf("")
    val errorPattern = mutableStateOf(false)
    val validationResult = mutableStateOf("")
    val showPopup = mutableStateOf(false)
    val validating = mutableStateOf(false)
    val validationResults = mutableStateListOf<EmailValidationDTO>()

    val validate = mutableStateOf("")

    val smsSamples = mutableStateListOf<Pair<String, Boolean>>()
    val showSmsDialog = mutableStateOf(false)
    val aiFaile = mutableStateOf<Boolean>(false)

    fun loadSmsSamples() {
        if (phoneNumber.value.isNotEmpty()) {
            svc?.getSmsList(phoneNumber.value)?.let { list ->
                smsSamples.clear()
                list.map { Pair(it, false) }.forEach(smsSamples::add)
                showSmsDialog.value = true
            }
        }
    }

    fun isAIValid():Boolean = prefs?.llmEnabled?:false


    fun generateRegexWithAI() {
        val selectedSms = smsSamples.filter { it.second }.map { it.first }
        if (selectedSms.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                load.value = true
                progress.floatValue = 0.1f
                val prompt = navController?.context?.getString(R.string.promt_sms_expreg,selectedSms.joinToString("\n"))?:""
                llmService?.getAiResponse(prompt)?.onSuccess { response ->
                    progress.floatValue = 0.5f
                    pattern.value = response.trim().removeSurrounding("`").removePrefix("regex").trim()
                    progress.floatValue = 0.8f
                    viewModelScope.launch(Dispatchers.Main) {
                        navController?.let { Toast.makeText(it.context, R.string.ai_response, Toast.LENGTH_SHORT).show() }
                    }
                    progress.floatValue = 1.0f
                    load.value = false
                    showSmsDialog.value = false
                }?.onFailure {
                    progress.floatValue = 1.0f
                    load.value = false
                    aiFaile.value = true
                    showSmsDialog.value = false
                }
            }
        }
    }


    fun save(){
        if(smsPaid != null && !errorPhoneNumber.value && !errorPattern.value) {
            smsPaid?.takeIf { it.id == 0 }?.let{
                svc?.create(it)?.takeIf { it > 0 }?.let{
                    smsPaid = smsPaid!!.copy(id = it)
                    navController?.let { Toast.makeText(it.context, R.string.toast_save_successful, Toast.LENGTH_SHORT).show().also {
                        navController.popBackStack()
                    } }
                }?:navController?.let { Toast.makeText(it.context, R.string.toast_save_error, Toast.LENGTH_SHORT).show() }
            }
            smsPaid?.takeIf { it.id > 0 }?.let{
                svc?.update(it)?.takeIf { it }?.let{
                    navController?.let { Toast.makeText(it.context, R.string.toast_update_successful, Toast.LENGTH_SHORT).show().also {
                        navController.popBackStack()
                    }}
                }?:navController?.let { Toast.makeText(it.context, R.string.toast_update_error, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    fun clean(){
        phoneNumber.value = ""
        pattern.value = ""
        validate.value = ""
        account.value = null
        smsPaid = null
        progress.floatValue = 0.0f
        load.value = true
        errorPhoneNumber.value = false
        errorPattern.value = false
        errorAccount.value = false

    }

    fun validatePatternWithMessages(){
        validate()
        smsPaid?.let {dto->
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    validating.value = true
                    showPopup.value = true
                    validationResults.clear()
                    validationResult.value = ""
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
                            validationResult.value = "Not found sms"
                        }
                    }
                }
            }
        }
    }

    fun validate(){

        phoneNumber.value.takeIf { it.isNotEmpty() }?.let{
            errorPhoneNumber.value = false
        }?:errorPhoneNumber.let{
            it.value = true
        }

        pattern.value.takeIf { it.isNotEmpty() }?.let{
            errorPattern.value = false
        }?:errorPattern.let{
            it.value = true
        }

        account.value?.let{
            errorAccount.value = false
        }?:errorAccount.let{
            it.value = true
        }

        if(!errorPhoneNumber.value && !errorPattern.value && !errorAccount.value) {
            smsPaid = SMSPaidDTO(
                id = smsPaid?.id?:0,
                phoneNumber= phoneNumber.value,
                pattern = pattern.value,
                codeAccount = account.value?.first?:0,
                nameAccount = account.value?.second?:"",
                active = true
            )
        }
    }

    fun main() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                progress.floatValue = 0.1f
            }
            execute()
            withContext(Dispatchers.Main) {
                progress.floatValue = 1.0f
            }
        }
    }

    suspend fun execute(){
        svc?.let{
            codeSMS?.let {
                svc.getById(it)?.let{
                    smsPaid = it
                    phoneNumber.value = it.phoneNumber
                    pattern.value = it.pattern
                    progress.floatValue = 0.3f
                }
            }
        }
        accountSvc?.let {
            accountList.clear()
            accountLists.clear()
            it.getAll()?.takeIf { it.isNotEmpty() }?.forEach {
                accountLists.add(it)
                accountList.add(Pair(it.id,it.name))
            }
                .also { progress.floatValue = 0.6f }
            smsPaid?.let{sms->
                accountLists.firstOrNull { it.id == sms.codeAccount }?.let { account.value = Pair(it.id,it.name) }
            }
        }

        kindInterestRateList.clear()
        KindInterestRateEnum.values().map { Pair(it.getCode().toInt(), it.name)}.forEach(kindInterestRateList::add)
            .also { progress.floatValue = 0.8f }

        load.value = false
    }


}
