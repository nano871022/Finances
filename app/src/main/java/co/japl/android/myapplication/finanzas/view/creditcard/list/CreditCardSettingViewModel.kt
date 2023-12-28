package co.japl.android.myapplication.finanzas.view.creditcard.list

import android.widget.Toast
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import kotlinx.coroutines.runBlocking

class CreditCardSettingViewModel constructor(private val codCreditCard:Int,private val creditCardSettingSvc:ICreditCardSettingPort?,private val navController: NavController?):ViewModel() {

    var showProgress : MutableState<Boolean> = mutableStateOf(true)
    var progress : MutableFloatState = mutableFloatStateOf(0f)

    private var _list = listOf<CreditCardSettingDTO>()
    val list get() = _list

    fun onClick(){
        navController?.let { CreditCardSettingParams.newInstance(codCreditCard,it) }
    }

    fun delete(id:Int){
        if(creditCardSettingSvc?.let{it.delete(codCreditCard,id)} == true){
            navController?.let { Toast.makeText(it.context, R.string.delete_successfull,Toast.LENGTH_LONG).show() }
        }else{
            navController?.let { Toast.makeText(it.context,R.string.dont_deleted,Toast.LENGTH_LONG).show() }
        }
    }

    fun edit(codSetting:Int){
        navController?.let{CreditCardSettingParams.newInstance(codCreditCard,codSetting,it)}
    }


    fun main()= runBlocking{
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1f
    }

    suspend fun execute(){
        progress.floatValue = 0.4f
        creditCardSettingSvc?.let{it.getAll(codCreditCard)?.let {
            _list = it
            showProgress.value = false
        }}
        progress.floatValue = 0.8f
    }
}