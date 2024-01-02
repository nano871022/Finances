package co.japl.android.myapplication.finanzas.view.creditcard

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class CreditCardSettingViewModel constructor(private val codeCreditCard:Int?,private val codeCreditCardSetting:Int?,private val creditCardSvc:ICreditCardPort?,private val creditCardSettingSvc:ICreditCardSettingPort?,private val navController: NavController?) : ViewModel() {

    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(true)
    var newOne = mutableStateOf(true)

    var dto:CreditCardSettingDTO ? = null

    var creditCard:CreditCardDTO?= null

    var name = mutableStateOf("")
    var nameIsError = mutableStateOf(false)
    var value = mutableStateOf("")
    var valueIsError = mutableStateOf(false)
    var type = mutableStateOf("")
    var typeIsError = mutableStateOf(false)
    var active = mutableStateOf(false)
    var showButtons = mutableStateOf(false)

    fun validate(){
        name.value.takeIf { it.isEmpty() }?.let{ nameIsError.value = true}
        name.value.takeIf{it.isNotEmpty()}?.let{nameIsError.value = false}
        value.value.takeIf { it.isEmpty() || !NumbersUtil.isNumber(it) }?.let{ valueIsError.value = true}
        value.value.takeIf{ it.isNotEmpty() && NumbersUtil.isNumber(it)}?.let{ valueIsError.value = false}
        type.value?.takeIf { it.isEmpty() }?.let{typeIsError.value = true}
        type.value?.takeIf { it.isNotEmpty()}?.let {typeIsError.value = false }
        showButtons.value = !validation()
    }

    fun update(){
        dto?.let {
            it.name = name.value
            it.value = value . value
            it.type = type.value
            it.active = active.value.takeIf { it }?.let { 1 }?:0
        }
        if(creditCardSettingSvc?.update(dto!!) == true){
            navController?.navigateUp()
            Toast.makeText(navController?.context,R.string.toast_successful_update,Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(navController?.context,R.string.toast_dont_successful_update,Toast.LENGTH_LONG).show()
        }
    }

    private fun validation():Boolean{
        return nameIsError.value || valueIsError.value || typeIsError.value
    }

    fun create(){
        Log.d(javaClass.name,"<<<=== Create $dto")
        dto = CreditCardSettingDTO(id = 0, codeCreditCard = codeCreditCard?:0, name = "", value = "", type = "",create=LocalDateTime.now(), active = 1)
        dto?.let {
            it.name = name.value
            it.value = value . value
            it.type = type.value
            it.active = active.value.takeIf { it }?.let { 1 }?:0
        }
        dto?.let {  dto->
            creditCardSettingSvc?.create(dto)?.let{
                dto.id = it
                navController?.navigateUp()
                Toast.makeText(navController?.context, R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
            }?:Toast.makeText(navController?.context, R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()

        }

    }

    fun main()= runBlocking {
        progress.floatValue = 0.2f
        execute()
        progress.floatValue= 1f

    }

    suspend fun execute(){
        Log.d(javaClass.name,"execute CreditCard: $codeCreditCard Setting: $codeCreditCardSetting")
        progress.floatValue = 0.4f
      creditCardSvc?.getCreditCard(codeCreditCard?:0)?.let{
          creditCard = it
          showProgress.value= false
      }
        progress.floatValue= 0.6f
        creditCardSettingSvc?.get(codeCreditCard?:0,codeCreditCardSetting?:0)?.let {
            dto = it
        }
        progress.floatValue = 0.7f
        dto?.let {
            active.value = it.active.takeIf { it.toInt() > 0 }?.let { true }?:false
            newOne.value = it.id <= 0
            name.value = it.name
            value.value = it.value
            type.value = it.type
            showButtons.value = true
        }
        progress.floatValue = 0.8f
    }

}