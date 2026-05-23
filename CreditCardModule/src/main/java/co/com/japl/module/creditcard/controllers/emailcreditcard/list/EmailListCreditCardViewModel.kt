package co.com.japl.module.creditcard.controllers.emailcreditcard.list

import androidx.compose.runtime.mutableStateOf
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.module.creditcard.navigations.EmailListCreditCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailListCreditCardViewModel(val svc: IEmailCreditCardPort?, val navController: NavController?) : ViewModel(){

    val load = mutableStateOf(false)
    val list = mutableListOf<EmailCreditCardDTO>()

    fun create(){
        navController?.let{
            EmailListCreditCard.navigate(0,it)
        }
    }

    fun edit(id:Int){
        navController?.let{
            EmailListCreditCard.navigate(id,it)
        }
    }

    fun activate(id:Int,active:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                svc?.activate(id, active)?:false
            }.onFailure { e ->
                withContext(Dispatchers.Main) {

                }
            }.onSuccess { resp ->
                withContext(Dispatchers.Main) {
                    if (resp) {
                        main()
                    } else {

                    }
                }
            }
        }
    }

    fun delete(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                svc?.delete(id)?:false
            }.onFailure { e ->
                withContext(Dispatchers.Main) {

                }
            }.onSuccess { resp ->
                withContext(Dispatchers.Main) {
                    if (resp) {
                        main()
                    } else {

                    }
                }
            }
        }
    }

    fun clone(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                svc?.clone(id)?:false
            }.onFailure { e ->
                withContext(Dispatchers.Main) {

                }
            }.onSuccess { resp ->
                withContext(Dispatchers.Main) {
                    if (resp) {

                    } else {

                    }
                }
            }
        }
    }

    fun main () = runBlocking {
        load.value = true
        execution()
    }

    suspend fun execution(){
        svc?.getAll()?.let{
            list.clear()
            list.addAll(it)
            load.value = false
        }
    }

}