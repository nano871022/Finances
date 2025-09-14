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

class CreditCardListViewModel constructor(private val creditCardSvc:ICreditCardPort?) : ViewModel(){


    val list = mutableStateListOf<CreditCardDTO?>()

    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(true)

    fun onClick(navController: NavController){
        navController.let{CreditCard.navigate(it)}
    }

    fun goToSettings(id:Int, navController: NavController){
        navController.let { ListCreditCardSettings.navigate(id,it) }
    }

    fun edit(id:Int, navController: NavController){
        navController.let { CreditCard.navigate(id,it) }
    }

    fun delete(id:Int, navController: NavController){
        creditCardSvc?.let{
            if(it.delete(id)){
                Toast.makeText(navController.context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(navController.context, R.string.toast_dont_successful_deleted,Toast.LENGTH_LONG).show()
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