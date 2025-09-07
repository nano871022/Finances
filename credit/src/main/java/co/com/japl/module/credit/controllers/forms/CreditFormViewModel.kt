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
import co.japl.android.graphs.utils.NumbersUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreditFormViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val creditSvc: ICreditFormPort,
    @ApplicationContext private val context:Context
): ViewModel() {
    var progress = mutableFloatStateOf(0f)
    var showProgress = mutableStateOf(false)
    val snackbarHostState = SnackbarHostState()
    var id:Int = 0
    private val _creditDto =  MutableStateFlow<CreditDTO>(CreditDTO(
        id = 0,
        name = "",
        date = LocalDate.now(),
        tax = 0.0,
        periods = 0,
        value = 0.toBigDecimal(),
        quoteValue = 0.toBigDecimal(),
        kindOf = KindPaymentsEnums.ANNUAL,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE
    ))
    private val creditDTO = _creditDto.asStateFlow()

    val kindPayment = initialFieldState<Triple<Int,String, KindPaymentsEnums?>?>(
        savedStateHandle,
        "FORM_KIND_PAYMENT",
        initialValue = Triple( KindPaymentsEnums.MONTHLY.ordinal,  context.resources.getString(KindPaymentsEnums.MONTHLY.title), KindPaymentsEnums.MONTHLY),
        list=KindPaymentsEnums.entries.map{ Triple(it.month,context.resources.getString(it.title),it)},
        validator = { it != null },
        onValueChangeCallBack = { kind ->
            _creditDto.update {
            it.copy(kindOf = kind?.third?: KindPaymentsEnums.ANNUAL)
        } }
    )

    val creditDate = initialFieldState<LocalDate>(
        savedStateHandle,
        "FORM_CREDIT_DATE",
        initialValue = LocalDate.now(),
        validator = {it != null},
        onValueChangeCallBack = { date ->
            _creditDto.update {
                it.copy(date = date)
            }
        }
    )

    val name = initialFieldState<String>(
        savedStateHandle,
        "FORM_NAME",
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { name ->
            _creditDto.update {
                it.copy(name = name)
            }
        }
    )

    val value = initialFieldState(
        savedStateHandle,
        "FORM_VALUE",
        initialValue = "",
        validator = { it.isNotBlank() && NumbersUtil.isNumber(it) },
        onValueChangeCallBack = { value ->
            _creditDto.update {
                it.copy(value = NumbersUtil.toBigDecimal(value))
            }
        }
    )

    val rate = initialFieldState(
        savedStateHandle,
        "FORM_RATE",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { rate ->
            _creditDto.update {
                it.copy(tax = NumbersUtil.toDouble(rate))
            }
        }
    )

    val kindRate = initialFieldState<Triple<Int,String, KindOfTaxEnum>?>(
        savedStateHandle,
        "FORM_KIND_RATE",
        initialValue = Triple(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal,context.resources.getString(KindOfTaxEnum.ANUAL_EFFECTIVE.title),KindOfTaxEnum.ANUAL_EFFECTIVE),
        list = KindOfTaxEnum.entries.map{ Triple(it.ordinal,context.resources.getString(it.title),it) },
        validator = {it != null},
        onValueChangeCallBack = { kind ->
            _creditDto.update {
                it.copy( kindOfTax = kind?.third?: KindOfTaxEnum.ANUAL_EFFECTIVE)
            }
        }
    )

    val month = initialFieldState(
        savedStateHandle,
        "FORM_MONTH",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { month ->
            _creditDto.update {
                it.copy(periods = month.toInt())
            }
        }
    )

    val quoteCredit = initialFieldState(
        savedStateHandle,
        "FORM_QUOTE_CREDIT",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { quote ->
            _creditDto.update {
                it.copy(quoteValue = NumbersUtil.toBigDecimal(quote))
            }

        }
    )


    init{
        id = savedStateHandle.get<Int>("id") ?: 0
        id.takeIf{it > 0}?.let {
            _creditDto.update {
                it.copy(id = id)
            }
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
                    message = context.resources.getString(R.string.invalid_form),
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
        creditSvc.save(creditDTO.value).takeIf{it > 0}?.let { code ->
            _creditDto.update {
                it.copy(id = code)
            }
            showProgress.value = false
        }?.also {
            showProgress.value = false
            snackbarHostState.showSnackbar(
                message = context.resources.getString(R.string.save_success)
            )
        }
    }

    fun execute() = CoroutineScope(Dispatchers.IO).launch {
        showProgress.value = true
        running()
    }

    suspend fun running(){
        progress.floatValue = 0f
        id.let {
            progress.floatValue = 0.2f
            creditSvc.findCreditById(id = it).let{ credit ->
                progress.floatValue = 0.4f
                credit?.let{
                    progress.floatValue = 0.7f
                    val kindPaymentEnum:KindPaymentsEnums = credit.kindOf
                    val kindOfTaxPair = kindPayment.list.firstOrNull{ it?.first == kindPaymentEnum.month}
                    val kindRateEnum = credit.kindOfTax
                    val kindRatePair = kindRate.list[kindRateEnum.ordinal]

                    kindPayment.onValueChange(Triple(kindOfTaxPair?.first?:0,kindOfTaxPair?.second?:"",kindPaymentEnum))
                    creditDate.onValueChange(credit.date)
                    name.onValueChange(credit.name)
                    value.onValueChange(NumbersUtil.toString(credit.value))
                    rate.onValueChange(NumbersUtil.toString(credit.tax))
                    kindRate.onValueChange(Triple(kindRatePair?.first?:0,kindRatePair?.second?:"",kindRateEnum))
                    month.onValueChange(credit.periods.toString())
                }

                progress.floatValue = 0.8f
            }
        }
        progress.floatValue = 1f
        showProgress.value = false
    }

    fun backView(navController: NavController){
        navController.popBackStack()
    }

    fun clean(){
        kindPayment.reset(Triple( KindPaymentsEnums.MONTHLY.ordinal,  context.resources.getString(KindPaymentsEnums.MONTHLY.title), KindPaymentsEnums.MONTHLY))
        creditDate.reset(LocalDate.now())
        name.reset("")
        value.reset("")
        rate.reset("")
        kindRate.reset(Triple(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal,context.resources.getString(KindOfTaxEnum.ANUAL_EFFECTIVE.title),KindOfTaxEnum.ANUAL_EFFECTIVE))
        month.reset("")
    }

    fun amortization(navController: NavController){
        if(_creditDto.value.id <= 0) {
            viewModelScope.launch {
                snackbarHostState.showSnackbar(
                    message = context.resources.getString(R.string.invalid_option_amortization),
                    withDismissAction = true
                )
            }
        }else{
            CreditList.amortization(creditDTO.value, creditDTO.value.date, navController)
        }
    }

    fun executeCalculateQuote() = CoroutineScope(Dispatchers.IO).launch {
        calculateQuote()
    }

    suspend fun calculateQuote(){
        creditSvc.calculateQuoteCredit(
            value = creditDTO.value.value,
            rate = creditDTO.value.tax,
            kindRate = creditDTO.value.kindOfTax,
            month = creditDTO.value.periods
        )?.let { quote ->
            quoteCredit.onValueChange(NumbersUtil.toString(quote))
        }
    }

}