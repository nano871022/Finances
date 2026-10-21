package co.com.japl.module.creditcard.controllers.creditrate.lists

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

    var creditCard:MutableMap<CreditCardDTO?,List<TaxDTO>>? by mutableStateOf<MutableMap<CreditCardDTO?,List<TaxDTO>>?>(HashMap())

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
        viewModelScope.launch {
            creditRateSvc?.let { svc ->
                val success = withContext(Dispatchers.IO) {
                    svc.delete(code)
                }
                if (success) {
                    Toast.makeText(context, R.string.toast_successful_deleted, Toast.LENGTH_SHORT)
                        .show()
                    main()
                } else {
                    Toast.makeText(context, R.string.toast_dont_successful_deleted, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun clone(code:Int){
        viewModelScope.launch {
            creditRateSvc?.let { svc ->
                val success = withContext(Dispatchers.IO) {
                    svc.clone(code)
                }
                if (success) {
                    Toast.makeText(context, R.string.toast_successful_cloned, Toast.LENGTH_SHORT)
                        .show()
                    main()
                } else {
                    Toast.makeText(context, R.string.toast_dont_successful_cloned, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun enable(code:Int){
        viewModelScope.launch {
            creditRateSvc?.let { svc ->
                val success = withContext(Dispatchers.IO) {
                    svc.enable(code)
                }
                if (success) {
                    Toast.makeText(context, R.string.toast_successful_enabled, Toast.LENGTH_SHORT)
                        .show()
                    main()
                } else {
                    Toast.makeText(context, R.string.toast_dont_successful_enabled, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun disable(code:Int){
        viewModelScope.launch {
            creditRateSvc?.let { svc ->
                val success = withContext(Dispatchers.IO) {
                    svc.disable(code)
                }
                if (success) {
                    Toast.makeText(context, R.string.toast_successful_disabled, Toast.LENGTH_SHORT)
                        .show()
                    main()
                } else {
                    Toast.makeText(context, R.string.toast_dont_successful_disabled, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun edit(codeCreditCard:Int, codeCreditRate:Int){
        navController?.let { ListCreditRate.navigate(codeCreditCard,codeCreditRate,navigate = navController)}
    }

    fun main() {
        viewModelScope.launch {
            showProgress.value = true
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
