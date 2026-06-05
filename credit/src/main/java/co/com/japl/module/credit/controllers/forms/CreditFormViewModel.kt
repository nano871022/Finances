package co.com.japl.module.credit.controllers.forms


import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.module.credit.navigations.CreditList
import co.com.japl.module.credit.params.CreditFixListParams
import co.com.japl.module.credit.params.CreditFixParams
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.ui.utils.initialFieldState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreditFormViewModel @Inject constructor(
    private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val creditSvc: ICreditFormPort?
) : ViewModel(){

    var navController: NavController? = null
    private val id: Int? = CreditFixParams.download(savedStateHandle)[CreditFixParams.Params.PARAMS_CREDIT_CODE]?.takeIf { it as Long > 0 }?.let{(it as Long).toInt()}

    var showProgress = mutableStateOf(false)
    
    private val _creditDto = MutableStateFlow<CreditDTO>(CreditDTO(
        id = id ?: 0,
        name = "",
        date = LocalDate.now(),
        tax = 0.0,
        periods = 0,
        value = BigDecimal.ZERO,
        quoteValue = BigDecimal.ZERO,
        kindOf = KindPaymentsEnums.MONTHLY,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE
    ))
    val creditDTO: StateFlow<CreditDTO> = _creditDto

    val kindPayment = initialFieldState(
        savedStateHandle,
        "kindPayment",
        initialValue = Triple(KindPaymentsEnums.MONTHLY.ordinal, context.getString(KindPaymentsEnums.MONTHLY.title), KindPaymentsEnums.MONTHLY),
        onValueChangeCallBack = { triple ->
            _creditDto.value = _creditDto.value.copy(kindOf = triple.third!!)
        }
    )

    val creditDate = initialFieldState(
        savedStateHandle,
        "creditDate",
        initialValue = DateUtils.localDateToString(LocalDate.now()),
        validator = {it.isNotBlank() && DateUtils.isDateValid(it)},
        onValueChangeCallBack = { date ->
            _creditDto.value = _creditDto.value.copy(date = DateUtils.toLocalDate(date))
        }
    )

    val name = initialFieldState(
        savedStateHandle,
        "name",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = { name ->
            _creditDto.value = _creditDto.value.copy(name = name)
            validate()
        }
    )

    val value = initialFieldState(
        savedStateHandle,
        "value",
        initialValue = "",
        validator = { it.isNotBlank() && NumbersUtil.isNumber(it) && NumbersUtil.toBigDecimal(it) > BigDecimal.ZERO },
        onValueChangeCallBack = { value ->
            _creditDto.value = _creditDto.value.copy(value = value.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            validate()
        }
    )

    val rate = initialFieldState(
        savedStateHandle,
        "rate",
        initialValue = "",
        validator = { it.isNotBlank() && NumbersUtil.isNumber(it) && NumbersUtil.toBigDecimal(it) > BigDecimal.ZERO },
        onValueChangeCallBack = { rate ->
            if(NumbersUtil.isNumber(rate)) {
                _creditDto.value = _creditDto.value.copy(tax = rate.toDouble())
                validate()
            }
        }
    )

    val kindRate = initialFieldState(
        savedStateHandle,
        "kindRate",
        initialValue = Triple(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal, KindOfTaxEnum.ANUAL_EFFECTIVE.value, KindOfTaxEnum.ANUAL_EFFECTIVE),
        onValueChangeCallBack = { triple ->
            _creditDto.value = _creditDto.value.copy(kindOfTax = triple.third)
            validate()
        }
    )

    val month = initialFieldState(
        savedStateHandle,
        "month",
        initialValue = "",
        validator = { it.isNotBlank() && NumbersUtil.isNumber(it) && NumbersUtil.toBigDecimal(it) > BigDecimal.ZERO },
        onValueChangeCallBack = { month ->
            _creditDto.value = _creditDto.value.copy(periods = month.toIntOrNull() ?: 0)
            validate()
        }
    )

    val quoteCredit = initialFieldState(
        savedStateHandle,
        "quoteCredit",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { quote ->
            _creditDto.value = _creditDto.value.copy(quoteValue = NumbersUtil.toBigDecimal(quote))
        }
    )

    init {
        execute()
    }

    fun onSubmitFormClicked() {
        if (validate()) {
            executeSave()
        }
    }

    fun validate(): Boolean {
        if(name.validate() && value.validate() && rate.validate() && month.validate()){

            calculateQuote()
            return true
        }
        return false
    }

   private fun executeSave() = viewModelScope.launch {
        showProgress.value = true
        withContext(Dispatchers.IO) {
            creditSvc?.save(_creditDto.value)
        }
        navController?.popBackStack()
        showProgress.value = false
    }

    fun execute() = viewModelScope.launch {
        showProgress.value = true
        running()
        showProgress.value = false
    }

    private suspend fun running() {
        id?.takeIf { it > 0 }?.let { code ->
            withContext(Dispatchers.IO) {
                creditSvc?.findCreditById(code)
            }?.let { dto ->
                _creditDto.value = dto
                name.onValueChange(dto.name)
                value.onValueChange(dto.value.toString())
                rate.onValueChange(dto.tax.toString())
                month.onValueChange(dto.periods.toString())
                quoteCredit.onValueChange(NumbersUtil.toString(dto.quoteValue))
                creditDate.onValueChange(DateUtils.localDateToString(dto.date))
                kindPayment.onValueChange(Triple(dto.kindOf.ordinal, context.getString(dto.kindOf.title), dto.kindOf))
                kindRate.onValueChange(Triple(dto.kindOfTax.ordinal, dto.kindOfTax.name, dto.kindOfTax))
            }
        }
    }

    fun clean() {
        name.onValueChange("")
        value.onValueChange("")
        rate.onValueChange("")
        month.onValueChange("")
        quoteCredit.onValueChange("")
        creditDate.onValueChange(DateUtils.localDateToString(LocalDate.now()))
    }

    fun calculateQuote() = viewModelScope.launch{
        creditSvc?.let {svc ->
                    withContext(Dispatchers.IO) {
                        svc.calculateQuoteCredit(creditDTO.value.value,creditDTO.value.tax,creditDTO.value.kindOfTax, creditDTO.value.periods)
                    }.let{
                        quoteCredit.onValueChange(NumbersUtil.toString(it))
                    }
        }
    }
}
