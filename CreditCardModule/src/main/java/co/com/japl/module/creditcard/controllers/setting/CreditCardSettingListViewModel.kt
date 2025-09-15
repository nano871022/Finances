package co.com.japl.module.creditcard.controllers.setting

import android.widget.Toast
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.CreditCardSetting
import kotlinx.coroutines.runBlocking

class CreditCardSettingListViewModel constructor(private val savedStateHandle: SavedStateHandle, private val creditCardSettingSvc:ICreditCardSettingPort):ViewModel() {
    private val codCreditCard:Int
    var showProgress : MutableState<Boolean> = mutableStateOf(true)
    var progress : MutableFloatState = mutableFloatStateOf(0f)

    var list = mutableStateListOf<CreditCardSettingDTO>()

    init{
        codCreditCard = savedStateHandle.get<Int>("codeCreditCard")!!
    }

    fun onClick(navController: NavController){
        CreditCardSetting.navigate(codCreditCard,navController)
    }

    fun delete(id:Int,navController: NavController){
        if(creditCardSettingSvc.delete(codCreditCard,id)){
            navController.navigateUp()
            Toast.makeText(navController.context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(navController.context,R.string.toast_dont_successful_deleted,Toast.LENGTH_LONG).show()
        }
    }

    fun edit(codSetting:Int,navController: NavController){
        CreditCardSetting.navigate(codCreditCard,codSetting,navController)
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