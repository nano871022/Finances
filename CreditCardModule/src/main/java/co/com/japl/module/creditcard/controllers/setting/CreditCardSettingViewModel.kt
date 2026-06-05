package co.com.japl.module.creditcard.controllers.setting

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.module.creditcard.params.CreditCardSettingParams
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import co.com.japl.ui.utils.initialFieldState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CreditCardSettingViewModel @Inject constructor(
    private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val creditCardSvc:ICreditCardPort,
    private val creditCardSettingSvc:ICreditCardSettingPort
) : ViewModel() {
    private val paramsArgs = CreditCardSettingParams.download(savedStateHandle)
    private val codeCreditCard: Int? = paramsArgs[CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]
    private val codeCreditCardSetting: Int? = paramsArgs[CreditCardSettingParams.Params.ARG_ID]
    var navController: NavController? = null


    var showProgress = mutableStateOf(true)
    var newOne = mutableStateOf(true)

    var dto = MutableStateFlow(CreditCardSettingDTO(
        id = 0,
        codeCreditCard = 0,
        name = "",
        value = "0",
        type = "",
        create = LocalDateTime.now(),
        active = 1
    ))

    var creditCard:CreditCardDTO?= null

    var name = initialFieldState(
        savedStateHandler = savedStateHandle,
        key = "FORM_CREDIT_CARD_SETTING_NAME",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = {
            dto.update { upd->
                upd.copy(name=it)
            }
            validate()
        }
    )

    var value = initialFieldState(
        savedStateHandler = savedStateHandle,
        key = "FORM_CREDIT_CARD_SETTING_VALUE",
        initialValue = dto.value.value?:"",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = {
            dto.update { upd->
                upd.copy(value=it)
            }
            validate()
        }
    )

    var type = initialFieldState(
        savedStateHandler = savedStateHandle,
        key = "FORM_CREDIT_CARD_SETTING_TYPE",
        initialValue = dto.value.type,
        validator = { it.isNotBlank() },
        onValueChangeCallBack = {
            dto.update{ upd->
                upd.copy(type = it)
            }
            validate()
        }
    )

    var active = initialFieldState<Boolean>(
        savedStateHandler = savedStateHandle,
        key = "FORM_CREDIT_CARD_SETTING_ACTIVE",
        initialValue = dto.value.active>0,
        validator = { true },
        onValueChangeCallBack = {
            dto.update{upd->
                upd.copy(active = if(it) 1 else 0)
            }
            validate()
        }
    )
    var showButtons = mutableStateOf(false)

    fun validate(){
        showButtons.value = name.validate() &&
                            value.validate() &&
                            type.validate()

    }

    fun update(){
        if(creditCardSettingSvc?.update(dto.value) == true){
            navController?.navigateUp()
            Toast.makeText(navController?.context, R.string.toast_successful_update,Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(navController?.context,R.string.toast_dont_successful_update,Toast.LENGTH_LONG).show()
        }
    }

    fun create(){
        dto?.let {  dto->
            creditCardSettingSvc.create(dto.value)?.let{
                dto.value.id = it
                navController?.navigateUp()
                Toast.makeText(navController?.context, R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
            }?:Toast.makeText(navController?.context, R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
        }
    }

    fun execute() = viewModelScope.launch{
        Log.d(javaClass.name,"execute CreditCard: $codeCreditCard Setting: $codeCreditCardSetting")

        withContext(Dispatchers.IO){
            creditCardSvc.getCreditCard(codeCreditCard?:0)
        }?.let{
          creditCard = it
            dto.value.codeCreditCard = it.id
      }

        withContext(Dispatchers.IO){
            creditCardSettingSvc.get(codeCreditCard?:0,codeCreditCardSetting?:0)
        }?.let {
            dto.update { upd->
                it
            }
            active.onValueChange(it.active > 0)
            newOne.value = it.id <= 0
            name.onValueChange(it.name)
            value.onValueChange(it.value)
            type.onValueChange(it.type)
            showButtons.value = true
        }

        showProgress.value= false
    }

}
