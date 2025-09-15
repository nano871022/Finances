package co.com.japl.module.creditcard.controllers.smscreditcard.form

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.module.creditcard.R
import kotlinx.coroutines.runBlocking

class SmsCreditCardViewModel constructor(private val savedStateHandle: SavedStateHandle,private val svc:ISMSCreditCardPort,private val creditCardSvc:ICreditCardPort): ViewModel() {
    private val codeSMSCC:Int?

    val  load = mutableStateOf(true)
    val  progress = mutableFloatStateOf(0.0f)

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

    val validate = mutableStateOf("")

    init{
        codeSMSCC = savedStateHandle.get<Int>("code_sms_credit_Card")
    }

    fun save(navController: NavController){
        if(smsCreditCard != null && !errorKindInterestRate.value && !errorPhoneNumber.value && !errorPattern.value) {
            smsCreditCard?.takeIf { it.id == 0 }?.let{
                svc?.create(it)?.takeIf { it > 0 }?.let{
                    smsCreditCard = smsCreditCard!!.copy(id = it)
                    navController?.let { Toast.makeText(it.context, R.string.toast_successful_insert, Toast.LENGTH_SHORT).show().also {
                        navController.popBackStack()
                    } }
                }?:navController?.let { Toast.makeText(it.context, R.string.toast_unsuccessful_insert, Toast.LENGTH_SHORT).show() }
            }
            smsCreditCard?.takeIf { it.id > 0 }?.let{
                svc?.update(it)?.takeIf { it }?.let{
                    navController?.let { Toast.makeText(it.context, R.string.toast_successful_update, Toast.LENGTH_SHORT).show().also {
                        navController.popBackStack()
                    }}
                }?:navController?.let { Toast.makeText(it.context, R.string.toast_dont_successful_update, Toast.LENGTH_SHORT).show() }
            }
        }
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
        smsCreditCard?.let { dto ->
            svc?.let {
                validationResult.value = ""
                runCatching {
                    it.validateMessagePattern(dto)
                }.onFailure {
                    validationResult.value = "Error: ${it.message}"
                }.onSuccess {
                    it?.takeIf { it.isNotEmpty() }?.forEach {
                        validationResult.value += it + "\n\n"
                    } ?: validationResult.let { it.value = "Not found sms" }
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

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    suspend fun execute(){
        svc?.let{
            codeSMSCC?.let {
                svc.getById(it)?.let{
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
            it.getAll()?.takeIf { it.isNotEmpty() }?.forEach {
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

        load.value = false
    }


}