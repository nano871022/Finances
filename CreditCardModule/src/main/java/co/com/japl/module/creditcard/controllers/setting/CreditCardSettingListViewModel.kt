package co.com.japl.module.creditcard.controllers.setting

import android.widget.Toast
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.CreditCardSetting
import co.com.japl.module.creditcard.params.ListCreditCardSettingParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject

@HiltViewModel
class CreditCardSettingListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val creditCardSettingSvc:ICreditCardSettingPort?
):ViewModel() {

    private val codCreditCard: Int = savedStateHandle.get<Int>(ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD) ?: 0
    var navController: NavController? = null

    var showProgress : MutableState<Boolean> = mutableStateOf(true)
    var progress : MutableFloatState = mutableFloatStateOf(0f)

    var list = mutableStateListOf<CreditCardSettingDTO>()

    fun onClick(){
        navController?.let { CreditCardSetting.navigate(codCreditCard,it) }
    }

    fun delete(id:Int){
        if(creditCardSettingSvc?.let{it.delete(codCreditCard,id)} == true){
            navController?.navigateUp()
            navController?.let { Toast.makeText(it.context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show() }
        }else{
            navController?.let { Toast.makeText(it.context,R.string.toast_dont_successful_deleted,Toast.LENGTH_LONG).show() }
        }
    }

    fun edit(codSetting:Int){
        navController?.let{CreditCardSetting.navigate(codCreditCard,codSetting,it)}
    }


    fun main()= runBlocking{
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1f
    }

    suspend fun execute(){
        progress.floatValue = 0.4f
        creditCardSettingSvc?.let{it.getAll(codCreditCard)?.let {
            list.clear()
            list.addAll( it)
            showProgress.value = false
        }}
        progress.floatValue = 0.8f
    }
}
