package co.com.japl.module.creditcard.controllers.creditrate.lists

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.ListCreditRate
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreditRateListViewModel(private val context:Context?, private val creditCardSvc:ICreditCardPort?, private val creditRateSvc:ITaxPort?, private val navController: NavController?):ViewModel() {

    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(true)

    var creditCard:MutableMap<CreditCardDTO?,List<TaxDTO>>? = HashMap<CreditCardDTO?,List<TaxDTO>>()

    fun add(){
        navController?.let { ListCreditRate.navigate(navigate = it)}
    }

    fun add(codeCreditCard:Int?){
        if(codeCreditCard == null){
            add()
        }else {
            navController?.let { ListCreditRate.navigate(codeCreditCard,navigate = it)}
        }
    }

    fun delete(code:Int){
        creditRateSvc?.let {
            if (creditRateSvc.delete(code)) {
                val found =
                    creditCard?.entries?.first { entry -> entry.value.first { it.id == code } != null }
                creditCard?.get(found?.key)?.let {
                    val list = it.filter { it.id != code }
                    val map = creditCard?.toMutableMap()
                    map?.set(found?.key, list)
                    creditCard = map
                }

                Toast.makeText(context, R.string.toast_successful_deleted, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, R.string.toast_dont_successful_deleted, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun clone(code:Int){
        creditRateSvc?.let {
            if (creditRateSvc.clone(code)) {
                Toast.makeText(context, R.string.toast_successful_cloned, Toast.LENGTH_SHORT)
                    .show().also {
                        showProgress.value = true
                    }
            } else {
                Toast.makeText(context, R.string.toast_dont_successful_cloned, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun enable(code:Int){
        creditRateSvc?.let {


            if (creditRateSvc.enable(code)) {
                Toast.makeText(context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun disable(code:Int){
        creditRateSvc?.let {
            if (creditRateSvc.disable(code)) {
                Toast.makeText(context, R.string.toast_successful_disabled, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, R.string.toast_dont_successful_disabled, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun edit(codeCreditCard:Int, codeCreditRate:Int){
        navController?.let { ListCreditRate.navigate(codeCreditCard,codeCreditRate,navigate = navController)}
    }

    fun main() {
        viewModelScope.launch {
            progress.floatValue = 0.1f
            execute()
            progress.floatValue = 1f
        }
    }

    suspend fun execute() {
        creditCardSvc?.let { svc ->
            val result = withContext(Dispatchers.IO) {
                svc.getAll()?.let { list ->
                    progress.floatValue = 0.5f
                    list.flatMap {
                        creditRateSvc?.let { svcTax ->
                            svcTax.getByCreditCard(it.id)?.sortedByDescending { it.create }
                        } ?: listOf()
                    }.groupBy { rate ->
                        list.find {
                            it.id == rate.codCreditCard
                        }
                    }
                }
            }
            progress.floatValue = 0.8f
            creditCard = result?.toMutableMap()
            showProgress.value = false
        }
    }

}