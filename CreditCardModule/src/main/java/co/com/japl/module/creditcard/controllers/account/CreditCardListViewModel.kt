package co.com.japl.module.creditcard.controllers.account

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.CreditCard
import co.com.japl.module.creditcard.navigations.ListCreditCardSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreditCardListViewModel @Inject constructor(private val creditCardSvc:ICreditCardPort?) : ViewModel(){

    private var navController: NavController? = null

    fun setNavController(navController: NavController){
        this.navController = navController
    }


    val list = mutableStateListOf<CreditCardDTO?>()

    var showProgress = mutableStateOf(true)

    fun onClick(){
        navController?.let{CreditCard.navigate(it)}
    }

    fun goToSettings(id:Int){
        navController?.let { ListCreditCardSettings.navigate(id,it) }
    }

    fun edit(id:Int){
        navController?.let { CreditCard.navigate(id,it) }
    }

    fun delete(id:Int){
        creditCardSvc?.let{
            if(it.delete(id)){
                Toast.makeText(navController?.context, R.string.toast_successful_deleted,Toast.LENGTH_LONG).show()
                reload()
            }else{
                Toast.makeText(navController?.context, R.string.toast_dont_successful_deleted,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun reload() {
        viewModelScope.launch {
            execute()
        }
    }

    suspend fun execute() {
        showProgress.value = true
        creditCardSvc?.let { svc ->
            val result = withContext(Dispatchers.IO) {
                svc.getAll()
            }
            list.clear()
            result?.let(list::addAll)
        }
        showProgress.value = false
    }

}

