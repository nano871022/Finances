package co.com.japl.module.credit.controllers.forms

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.module.credit.R
import co.com.japl.module.credit.navigations.CreditList
import co.japl.android.myapplication.utils.NumbersUtil
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@ViewModelScoped
class CreditFormViewModel constructor(var id:Int?,val creditSvc: ICreditFormPort?,val context:Context?, val navController: NavController?): ViewModel() {
    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(true)
    private val _uiState = MutableStateFlow(CreditFormState())
    val uiState: StateFlow<CreditFormState> = _uiState.asStateFlow()
    val kindPayment = mutableStateOf(KindPaymentsEnums.entries.map{ Pair(it.month,context?.resources?.getString(it.title)?:"Invalid")})
    val kindRate = mutableStateOf(KindOfTaxEnum.entries.toTypedArray().mapIndexed { index, kindOfTaxEnum -> Pair(index,context?.resources?.getString(kindOfTaxEnum.title)?:"Invalid") })
    val snackbarHostState = SnackbarHostState()
    val isErrorKindPayment = mutableStateOf(false)
    val isErrorKindRate = mutableStateOf(false)
    val isErrorDate = mutableStateOf(false)
    val isErrorName = mutableStateOf(false)
    val isErrorValue = mutableStateOf(false)
    val isErrorRate = mutableStateOf(false)
    val isErrorMonth = mutableStateOf(false)

    init{
        execute()
    }

    fun onKindPaymentChange(id:Int?,kindPayment: String?) {
        val error = if (kindPayment?.isBlank() == true) "Required" else null
        isErrorKindPayment.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                kindPayment = Triple(id?:0,kindPayment?:"",KindPaymentsEnums.findByIndex(id)),
                kindPaymentError = error,
                isFormValid = validate()
            )
        }
    }

    fun onCreditDateChange(creditDate: LocalDate) {
        val error = if (creditDate.isAfter(LocalDate.now())) "Required" else null
        isErrorDate.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                creditDate = creditDate,
                creditDateError = error,
                isFormValid = validate()
            )
        }
    }

    fun onNameChange(name: String) {
        val error = if (name.isBlank()) "Required" else null
        isErrorName.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                nameError = error,
                isFormValid = validate()
            )
        }
    }

    fun onValueChange(value: String) {

        val error = if (NumbersUtil.isNumber(value) && NumbersUtil.toBigDecimal(value).compareTo(BigDecimal.ZERO) == 0) "Required" else null
        isErrorValue.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                value = value,
                valueAmt = if (NumbersUtil.isNumber(value) && NumbersUtil.toBigDecimal(value).compareTo(BigDecimal.ZERO) != 0) NumbersUtil.toBigDecimal(value) else BigDecimal.ZERO,
                valueError = error,
                isFormValid = validate()
            )
        }
    }

    fun onRateChange(rate: String) {
        val error = if (NumbersUtil.isNumber(rate) && NumbersUtil.toDouble(rate).compareTo(0.0) == 0) "Required" else null
        Log.d("CreditFormViewModel","onRateChange $rate $error")
        isErrorRate.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                rate = rate,
                rateAmt = if (NumbersUtil.isNumber(rate) && NumbersUtil.toDouble(rate).compareTo(0.0) != 0) NumbersUtil.toDouble(rate) else 0.0,
                rateError = error,
                isFormValid = validate()
            )
        }
    }

    fun onKindRateChange(id:Int?,kindRate: String?) {
        val error = if (kindRate?.isBlank() == true) "Required" else null
        isErrorKindRate.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                kindRate = Triple(id?:0,kindRate?:"",KindOfTaxEnum.findByIndex(id)),
                kindRateError = error,
                isFormValid = validate()
            )
        }
    }

    fun onMonthChange(month: String) {
        val error = if (NumbersUtil.isIntNumber(month) && month.toInt().compareTo(0) == 0) "Required" else null
        isErrorMonth.value = error?.isNotBlank()?:false
        _uiState.update { currentState ->
            currentState.copy(
                month = if (NumbersUtil.isIntNumber(month) && month.toInt().compareTo(0) != 0) month.toInt() else 0,
                monthError = error,
                isFormValid = validate()
            )
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
        if(_uiState.value.kindPayment.second.isBlank()){
            valid = false
            isErrorKindPayment.value = true
            _uiState.update { currentState ->
                currentState.copy(
                    kindPaymentError = "Required"
                )
            }
        }

                if (_uiState.value.creditDate.isAfter(LocalDate.now())) {
                    valid = false
                    isErrorDate.value = true
                    _uiState.update { currentState ->
                        currentState.copy(
                            creditDateError = "Required"
                        )
                    }
                }
                if(_uiState.value.name.isBlank()) {
                    valid = false
                    isErrorName.value = true
                    _uiState.update { currentState ->
                        currentState.copy(
                            nameError = "Required"
                        )
                    }
                }
                if(_uiState.value.valueAmt.compareTo(BigDecimal.ZERO) == 0) {
                    valid = false
                    isErrorValue.value = true
                    _uiState.update { currentState ->
                        currentState.copy(
                            valueError = "Required"
                        )
                    }
                }
                if(_uiState.value.rateAmt.compareTo(0.0) == 0) {
                    valid = false
                    isErrorRate.value = true
                    _uiState.update { currentState ->
                        currentState.copy(
                            rateError = "Required"
                        )
                    }
                }else{
                    isErrorRate.value = false
                }

                if(_uiState.value.kindRate.second.isBlank() ) {
                    valid = false
                    isErrorKindRate.value = true
                    _uiState.update { currentState ->
                        currentState.copy(
                            kindRateError = "Required"
                        )
                    }
                }
                if(_uiState.value.month == 0) {
                    valid = false
                    isErrorMonth.value = true
                    _uiState.update { currentState ->
                        currentState.copy(
                            monthError = "Required"
                        )
                    }
                }
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
                _uiState.update { currentState ->
                    progress.floatValue = 0.7f
                    val kindPaymentEnum = KindPaymentsEnums.findByValue(credit.kindOf)
                    val kindOfTaxPair = kindPayment.value.get(kindPaymentEnum.ordinal)
                    val kindRateEnum = KindOfTaxEnum.findByValue(credit.kindOfTax)
                    val kindRatePair = kindRate.value.get(kindRateEnum.ordinal)
                    currentState.copy(
                        kindPayment = Triple(kindOfTaxPair.first,kindOfTaxPair.second,kindPaymentEnum),
                        creditDate = credit.date,
                        name = credit.name,
                        value = credit.toString(),
                        valueAmt = credit.value,
                        rate = credit.tax.toString(),
                        rateAmt = credit.tax,
                        kindRate = Triple(kindRatePair.first,kindRatePair.second,kindRateEnum),
                        month = credit.periods
                    )
                }

                }
                defaultSelectValues()
                showProgress.value = false
                progress.floatValue = 0.8f
            }
        }
        progress.floatValue = 1f
    }

    private fun defaultSelectValues(){
        if(uiState.value.kindRate.third == null){
            val kindRatePair = kindRate.value.get(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal)
            _uiState.update { currentState ->
                currentState.copy(
                    kindRate = Triple(kindRatePair.first,kindRatePair.second,KindOfTaxEnum.ANUAL_EFFECTIVE  )
                )
            }
        }
        if(uiState.value.kindPayment.third == null){
            val kindPaymentsPair = kindPayment.value.get(KindPaymentsEnums.MONTHLY.ordinal)
            _uiState.update { currentState ->
                currentState.copy(
                    kindPayment = Triple(kindPaymentsPair.first,kindPaymentsPair.second,KindPaymentsEnums.MONTHLY  )
                )
            }
        }
    }

    fun backView(){
        navController?.popBackStack()
    }

    fun clean(){
        _uiState.update { currentState ->
            currentState.copy(
                kindPayment = Triple(0,"",null),
                creditDate = LocalDate.now(),
                name = "",
                value = "",
                valueAmt = BigDecimal.ZERO
                ,rate = "",
                rateAmt = 0.0,
                kindRate = Triple(0,"",null),
                month = 0
            )
        }
        defaultSelectValues()
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
                CreditList.amortization(getCreateCreditDTO(), uiState.value.creditDate, it)
            }
        }
    }

    fun getCreateCreditDTO():CreditDTO{
        return CreditDTO(
            id = id?:0,
            name = _uiState.value.name,
            date = _uiState.value.creditDate,
            tax = _uiState.value.rateAmt,
            periods = _uiState.value.month,
            value = _uiState.value.valueAmt,
            quoteValue = _uiState.value.quoteCredit,
            kindOf = _uiState.value.kindPayment.third?.name?:"",
            kindOfTax = _uiState.value.kindRate.third?.getName()?:""
        )
    }

    fun executeCalculateQuote() = CoroutineScope(Dispatchers.IO).launch {
        calculateQuote()
    }

    suspend fun calculateQuote(){
        creditSvc?.calculateQuoteCredit(
            value = _uiState.value.valueAmt,
            rate = _uiState.value.rateAmt,
            kindRate = _uiState.value.kindRate.third?:KindOfTaxEnum.MONTHLY_EFFECTIVE,
            month = _uiState.value.month
        )?.let { quote ->
            _uiState.update{ currentState ->
               currentState.copy(
                   quoteCredit = quote
               )
            }
        }
    }

}