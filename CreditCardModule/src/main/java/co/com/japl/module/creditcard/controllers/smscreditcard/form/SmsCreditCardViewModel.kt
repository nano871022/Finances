package co.com.japl.module.creditcard.controllers.smscreditcard.form

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.Prefs
import com.google.android.material.snackbar.BaseTransientBottomBar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.time.Duration

@HiltViewModel(assistedFactory = SmsCreditCardViewModel.Factory::class)
class SmsCreditCardViewModel @AssistedInject constructor(@Assisted private val codeSMSCC:Int?, private val svc:ISMSCreditCardPort?, private val creditCardSvc:ICreditCardPort?, @Assisted private val navController: NavController?, private val llmService: ILLMService? = null, private val prefs: Prefs?=null): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(codeSMSCC: Int?, navController: NavController?): SmsCreditCardViewModel
    }

    val  load = mutableStateOf(true)
    val  progress = mutableFloatStateOf(0.0f)
    val aiFaile = mutableStateOf<Boolean>(false)

    private var creditCardLists = mutableListOf<CreditCardDTO>()
   private var smsCreditCard:SMSCreditCard? = null

    val creditCardList = mutableStateListOf<Pair<Int,String>>()
    val kindInterestRateList = mutableStateListOf<Pair<Int,String>>()

    var creditCard = mutableStateOf<Pair<Int,String>?>(null)
    val errorCreditCard = mutableStateOf(false)
    val kindInterestRate = mutableStateOf<Pair<Int,String>?>(null)
    val errorKindInterestRate = mutableStateOf(false)
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
    val llmModels = mutableStateListOf<Pair<Int, String>>()
    val selectedLLMModel = mutableStateOf<Pair<Int, String>?>(null)

    fun loadSmsSamples() {
        if (phoneNumber.value.isNotEmpty()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    svc?.getSmsList(phoneNumber.value)
                }?.let { list ->
                    smsSamples.clear()
                    list.map { Pair(it, false) }.forEach(smsSamples::add)
                    showSmsDialog.value = true
                }
            }
        }
    }

    fun generateRegexWithAI() {
        val selectedSms = smsSamples.filter { it.second }.map { it.first }
        if (selectedSms.isNotEmpty()) {
            viewModelScope.launch {
                load.value = true
                progress.floatValue = 0.1f
                val prompt = navController?.context?.getString(R.string.promt_sms_expreg, selectedSms.joinToString("\n"))?:""
                progress.floatValue = 0.3f
                withContext(Dispatchers.IO) {
                    llmService?.getAiResponse(prompt, selectedLLMModel.value?.second)
                }?.onSuccess { response ->
                    progress.floatValue = 0.6f
                    pattern.value = response.trim().removeSurrounding("`").removePrefix("regex").trim()
                    progress.floatValue = 1.0f
                    load.value = false
                    showSmsDialog.value = false
                    navController?.let { Toast.makeText(it.context, R.string.ai_response, Toast.LENGTH_SHORT).show() }
                }?.onFailure {
                    Log.e("SmsCreditCardViewModel", "Error: ${it.message}")
                    aiFaile.value=true
                    load.value = false
                    showSmsDialog.value = false
                    progress.floatValue = 1.0f
                }
            }
        }
    }


    fun save(){
        if(smsCreditCard != null && !errorKindInterestRate.value && !errorPhoneNumber.value && !errorPattern.value) {
            viewModelScope.launch {
                smsCreditCard?.takeIf { it.id == 0 }?.let{
                    withContext(Dispatchers.IO) {
                        svc?.create(it)
                    }?.takeIf { it > 0 }?.let{
                        smsCreditCard = smsCreditCard!!.copy(id = it)
                        navController?.let { Toast.makeText(it.context, R.string.toast_successful_insert, Toast.LENGTH_SHORT).show().also {
                            navController.popBackStack()
                        } }
                    }?:navController?.let { Toast.makeText(it.context, R.string.toast_unsuccessful_insert, Toast.LENGTH_SHORT).show() }
                }
                smsCreditCard?.takeIf { it.id > 0 }?.let{
                    withContext(Dispatchers.IO) {
                        svc?.update(it)
                    }?.takeIf { it }?.let{
                        navController?.let { Toast.makeText(it.context, R.string.toast_successful_update, Toast.LENGTH_SHORT).show().also {
                            navController.popBackStack()
                        }}
                    }?:navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_update, Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    fun isAIValid():Boolean{
        return prefs?.llmEnabled?:false
    }

    fun clean(){
        kindInterestRate.value = null
        phoneNumber.value = ""
        pattern.value = ""
        validate.value = ""
        creditCard.value = null
        smsCreditCard = null
        progress.floatValue = 0.0f
        load.value = true
        errorKindInterestRate.value = false
        errorPhoneNumber.value = false
        errorPattern.value = false
        errorCreditCard.value = false

    }

    fun validatePatternWithMessages() {
        validate()
        smsCreditCard?.let { dto ->
            viewModelScope.launch {
                validating.value = true
                showPopup.value = true
                validationResults.clear()
                validationResult.value = ""
                runCatching {
                    withContext(Dispatchers.IO) {
                        svc?.validateMessagePattern(dto) ?: emptyList()
                    }
                }.onFailure { e ->
                    validating.value = false
                    validationResult.value = "Error: ${e.message}"
                }.onSuccess { list ->
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

    fun validate(){

        kindInterestRate.value.takeIf { it != null }?.let{
            errorKindInterestRate.value = false
        }?:errorKindInterestRate.let{
            it.value = true
        }

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

        creditCard.value?.let{
            errorCreditCard.value = false
        }?:errorCreditCard.let{
            it.value = true
        }

        if(!errorKindInterestRate.value && !errorPhoneNumber.value && !errorPattern.value && !errorCreditCard.value) {
            smsCreditCard = SMSCreditCard(
                id = smsCreditCard?.id?:0,
                phoneNumber= phoneNumber.value,
                pattern = pattern.value,
                kindInterestRateEnum = KindInterestRateEnum.valueOf(kindInterestRate.value?.second?:""),
                codeCreditCard = creditCard.value?.first?:0,
                nameCreditCard = creditCard.value?.second?:"",
                active = true
            )
        }
    }

    fun main() {
        viewModelScope.launch {
            progress.floatValue = 0.1f
            execute()
            progress.floatValue = 1.0f
        }
    }

    suspend fun execute(){
        svc?.let{
            codeSMSCC?.let {
                withContext(Dispatchers.IO) {
                    svc.getById(it)
                }?.let{
                    smsCreditCard = it
                    kindInterestRate.value = it.kindInterestRateEnum.let{
                        Pair(it.getCode().toInt(),it.name)
                    }
                    phoneNumber.value = it.phoneNumber
                    pattern.value = it.pattern
                    progress.floatValue = 0.3f
                }
            }
        }
        creditCardSvc?.let {
            creditCardList.clear()
            creditCardLists.clear()
            withContext(Dispatchers.IO) {
                it.getAll()
            }?.takeIf { it.isNotEmpty() }?.forEach {
                creditCardLists.add(it)
                creditCardList.add(Pair(it.id,it.name))
            }
                .also { progress.floatValue = 0.6f }
            smsCreditCard?.let{sms->
                creditCardLists.firstOrNull { it.id == sms.codeCreditCard }?.let { creditCard.value = Pair(it.id,it.name) }
            }
        }

        kindInterestRateList.clear()
        KindInterestRateEnum.values().map { Pair(it.getCode().toInt(), it.name)}.forEach(kindInterestRateList::add)
            .also { progress.floatValue = 0.8f }

        withContext(Dispatchers.IO) {
            llmService?.getModels()
        }?.onSuccess { models ->
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