package co.com.japl.module.paid.controllers.sms.form

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.module.paid.R
import kotlinx.coroutines.runBlocking

class SmsViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val svc:ISMSPaidPort?,
    private val accountSvc:IAccountPort?): ViewModel() {

    private var codeSMS:Int = 0

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

    val validate = mutableStateOf("")

    init{
        savedStateHandle.get<Int>("code_sms_paid")?.let{
            codeSMS = it
        }
    }

    fun save(navController: NavController){
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
        smsPaid?.let {dto->
            svc?.let{
                validationResult.value = ""
                it.validateMessagePattern(dto)?.takeIf { it.isNotEmpty() }?.forEach{
                    validationResult.value += it+"\n\n"
                }?:validationResult.let{it.value = "Not found sms"}
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

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
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