package co.com.japl.module.credit.controllers.forms

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.CreditList
import co.com.japl.ui.utils.initialFieldState
import co.japl.android.myapplication.utils.NumbersUtil
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@ViewModelScoped
class CreditFormViewModel constructor(private val savedStateHandle: SavedStateHandle?=null, var id:Int?, val creditSvc: ICreditFormPort?, val context:Context?, val navController: NavController?): ViewModel() {
    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(false)
    val snackbarHostState = SnackbarHostState()

    val kindPayment = initialFieldState<Triple<Int,String, KindPaymentsEnums?>?>(
        savedStateHandle!!,
        "FORM_KIND_PAYMENT",
        initialValue = Triple( KindPaymentsEnums.MONTHLY.ordinal,  context?.resources?.getString(KindPaymentsEnums.MONTHLY.title)?:"", KindPaymentsEnums.MONTHLY),
        list=KindPaymentsEnums.entries.map{ Triple(it.month,context?.resources?.getString(it.title)?:"Invalid",it)},
        validator = { it != null },
        onValueChangeCallBack = {  }
    )

    val creditDate = initialFieldState<LocalDate>(
        savedStateHandle!!,
        "FORM_CREDIT_DATE",
        initialValue = LocalDate.now(),
        validator = {it != null},
        onValueChangeCallBack = {  }
    )

    val name = initialFieldState<String>(
        savedStateHandle!!,
        "FORM_NAME",
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = {  }
    )

    val value = initialFieldState(
        savedStateHandle!!,
        "FORM_VALUE",
        initialValue = "",
        validator = { it.isNotBlank() && NumbersUtil.isNumber(it) },
        onValueChangeCallBack = {  }
    )

    val rate = initialFieldState(
        savedStateHandle!!,
        "FORM_RATE",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = {  }
    )

    val kindRate = initialFieldState<Triple<Int,String, KindOfTaxEnum>?>(
        savedStateHandle!!,
        "FORM_KIND_RATE",
        initialValue = Triple(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal,context?.resources?.getString(KindOfTaxEnum.ANUAL_EFFECTIVE.title)?:"",KindOfTaxEnum.ANUAL_EFFECTIVE),
        list = KindOfTaxEnum.entries.map{ Triple(it.ordinal,context?.resources?.getString(it.title)?:"Invalid",it) },
        validator = {it != null},
        onValueChangeCallBack = {  }
    )

    val month = initialFieldState(
        savedStateHandle!!,
        "FORM_MONTH",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = {  }
    )

    val quoteCredit = initialFieldState(
        savedStateHandle!!,
        "FORM_QUOTE_CREDIT",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = {  }
    )


    init{
        id?.takeIf{it > 0}?.let {
            execute()
        }?:{
            showProgress.value = false
        }
    }

    fun onSubmitFormClicked(){
        if(validate()){
            executeSave()
        }else{
            viewModelScope.launch {
                snackbarHostState.showSnackbar(
                    message = context?.resources?.getString(R.string.invalid_form) ?: "Invalid",
                    withDismissAction = true
                )
            }
        }
    }

    fun validate():Boolean{
        var valid = true

        kindPayment.validate().not().or(kindPayment.error.value).takeIf { it }?.let { valid = false }
        creditDate.validate().not().or(creditDate.error.value).takeIf { it }?.let { valid = false }
        name.validate().not().or(name.error.value).takeIf { it }?.let { valid = false }
        value.validate().not().or(value.error.value).takeIf { it }?.let { valid = false }
        rate.validate().not().or(rate.error.value).takeIf { it }?.let { valid = false }
        kindRate.validate().not().or(kindRate.error.value).takeIf { it }?.let { valid = false }
        month.validate().not().or(month.error.value).takeIf { it }?.let { valid = false }
        return valid.also { it.takeIf { it }?.let { executeCalculateQuote() } }
    }

    fun executeSave() = CoroutineScope(Dispatchers.IO).launch {
        showProgress.value = true
        saveRunning()
    }

    suspend fun saveRunning(){
        creditSvc?.save(getCreateCreditDTO())?.takeIf{it > 0}.let {
            id = it
            showProgress.value = false
        }?.also {
            snackbarHostState.showSnackbar(
                message = context?.resources?.getString(R.string.save_success) ?: "Invalid"
            )
        }
    }

    fun execute() = CoroutineScope(Dispatchers.IO).launch {
        showProgress.value = true
        running()
    }

    suspend fun running(){
        progress.floatValue = 0f
        id?.let {
            progress.floatValue = 0.2f
            creditSvc?.findCreditById(id = it).let{ credit ->
                progress.floatValue = 0.4f
                credit?.let{
                    progress.floatValue = 0.7f
                    val kindPaymentEnum:KindPaymentsEnums = KindPaymentsEnums.findByValue(credit.kindOf)
                    val kindOfTaxPair = kindPayment.list.firstOrNull{ it?.first == kindPaymentEnum.month}
                    val kindRateEnum = KindOfTaxEnum.findByValue(credit.kindOfTax)
                    val kindRatePair = kindRate.list[kindRateEnum.ordinal]

                    kindPayment.onValueChange(Triple(kindOfTaxPair?.first?:0,kindOfTaxPair?.second?:"",kindPaymentEnum))
                    creditDate.onValueChange(credit.date)
                    name.onValueChange(credit.name)
                    value.onValueChange(NumbersUtil.toString(credit.value))
                    rate.onValueChange(NumbersUtil.toString(credit.tax))
                    kindRate.onValueChange(Triple(kindRatePair?.first?:0,kindRatePair?.second?:"",kindRateEnum))
                    month.onValueChange("credit.periods")
                }

                progress.floatValue = 0.8f
            }
        }
        progress.floatValue = 1f
        showProgress.value = false
    }

    fun backView(){
        navController?.popBackStack()
    }

    fun clean(){
        kindPayment.reset(Triple( KindPaymentsEnums.MONTHLY.ordinal,  context?.resources?.getString(KindPaymentsEnums.MONTHLY.title)?:"", KindPaymentsEnums.MONTHLY))
        creditDate.reset(LocalDate.now())
        name.reset("")
        value.reset("")
        rate.reset("")
        kindRate.reset(Triple(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal,context?.resources?.getString(KindOfTaxEnum.ANUAL_EFFECTIVE.title)?:"",KindOfTaxEnum.ANUAL_EFFECTIVE))
        month.reset("")
    }

    fun amortization(){
        if(id == 0 || id == null) {
            viewModelScope.launch {
                snackbarHostState.showSnackbar(
                    message = context?.resources?.getString(R.string.invalid_option_amortization)?:"Invalid",
                    withDismissAction = true
                )
            }
        }else{
            navController?.let {
                CreditList.amortization(getCreateCreditDTO(), creditDate.value.value, it)
            }
        }
    }

    fun getCreateCreditDTO():CreditDTO{
        return CreditDTO(
            id = id?:0,
            name = name.value.value,
            date = creditDate.value.value,
            tax = NumbersUtil.toDouble(rate.value.value),
            periods = month.value.value.toInt(),
            value = NumbersUtil.toBigDecimal(value.value.value),
            quoteValue = NumbersUtil.toBigDecimal(quoteCredit.value.value),
            kindOf = kindPayment.value.value?.second?: KindPaymentsEnums.MONTHLY.name,
            kindOfTax = kindRate.value.value?.second?: KindOfTaxEnum.ANUAL_EFFECTIVE.name
        )
    }

    fun executeCalculateQuote() = CoroutineScope(Dispatchers.IO).launch {
        calculateQuote()
    }

    suspend fun calculateQuote(){
        creditSvc?.calculateQuoteCredit(
            value = NumbersUtil.toBigDecimal(value.value.value),
            rate = NumbersUtil.toDouble(rate.value.value),
            kindRate = kindRate.value.value?.third?:KindOfTaxEnum.ANUAL_EFFECTIVE,
            month = month.value.value.toInt()
        )?.let { quote ->
            quoteCredit.onValueChange(NumbersUtil.toString(quote))
        }
    }

}