package co.com.japl.module.creditcard.controllers.account

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.CreditCard
import co.com.japl.module.creditcard.navigations.ListCreditCardSettings
import kotlinx.coroutines.runBlocking

import androidx.lifecycle.SavedStateHandle
import android.content.Context

class CreditCardListViewModel constructor(private val creditCardSvc:ICreditCardPort?, private val savedStateHandle: SavedStateHandle?) : ViewModel(){


    val list = mutableStateListOf<CreditCardDTO?>()

    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(true)

    fun onClick(navController: NavController){
        CreditCard.navigate(navController)
    }

    fun goToSettings(id:Int,navController: NavController){
        ListCreditCardSettings.navigate(id,navController)
    }

    fun edit(id:Int,navController: NavController){
        CreditCard.navigate(id,navController)
    }

    fun delete(id:Int,context:Context){
        creditCardSvc?.let{
            if(it.delete(id)){
                Toast.makeText(context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, R.string.toast_dont_successful_deleted,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1f
    }

    suspend fun execute(){
        progress.floatValue = 0.4f
        creditCardSvc?.let{
            list.clear()
            it.getAll()?.let(list::addAll)
            showProgress.value = false
        }
        progress.floatValue = 0.8f
    }
}