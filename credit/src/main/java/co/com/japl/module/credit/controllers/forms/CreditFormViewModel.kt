package co.com.japl.module.credit.controllers.forms


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.module.credit.params.CreditFixParams
import co.com.japl.ui.utils.initialFieldState
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
    private val savedStateHandle: SavedStateHandle,
    private val creditSvc: ICreditFormPort?
) : ViewModel(){

    var navController: NavController? = null
    private val id: Int? = savedStateHandle.get<Int>(CreditFixParams.Params.PARAMS_CREDIT_CODE)

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
        initialValue = Triple(KindPaymentsEnums.MONTHLY.ordinal, KindPaymentsEnums.MONTHLY.name, KindPaymentsEnums.MONTHLY),
        onValueChangeCallBack = { triple ->
            _creditDto.value = _creditDto.value.copy(kindOf = triple.third!!)
        }
    )

    val creditDate = initialFieldState(
        savedStateHandle,
        "creditDate",
        initialValue = LocalDate.now(),
        onValueChangeCallBack = { date ->
            _creditDto.value = _creditDto.value.copy(date = date)
        }
    )

    val name = initialFieldState(
        savedStateHandle,
        "name",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = { name ->
            _creditDto.value = _creditDto.value.copy(name = name)
        }
    )

    val value = initialFieldState(
        savedStateHandle,
        "value",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = { value ->
            _creditDto.value = _creditDto.value.copy(value = value.toBigDecimalOrNull() ?: BigDecimal.ZERO)
        }
    )

    val rate = initialFieldState(
        savedStateHandle,
        "rate",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = { rate ->
            _creditDto.value = _creditDto.value.copy(tax = rate.toDoubleOrNull() ?: 0.0)
        }
    )

    val kindRate = initialFieldState(
        savedStateHandle,
        "kindRate",
        initialValue = Triple(KindOfTaxEnum.ANUAL_EFFECTIVE.ordinal, KindOfTaxEnum.ANUAL_EFFECTIVE.name, KindOfTaxEnum.ANUAL_EFFECTIVE),
        onValueChangeCallBack = { triple ->
            _creditDto.value = _creditDto.value.copy(kindOfTax = triple.third)
        }
    )

    val month = initialFieldState(
        savedStateHandle,
        "month",
        initialValue = "",
        validator = { it.isNotBlank() },
        onValueChangeCallBack = { month ->
            _creditDto.value = _creditDto.value.copy(periods = month.toIntOrNull() ?: 0)
        }
    )

    val quoteCredit = initialFieldState(
        savedStateHandle,
        "quoteCredit",
        initialValue = "",
        onValueChangeCallBack = { quote ->
            _creditDto.value = _creditDto.value.copy(quoteValue = quote.toBigDecimalOrNull() ?: BigDecimal.ZERO)
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
        return name.validate() && value.validate() && rate.validate() && month.validate()
    }

    fun executeSave() = viewModelScope.launch {
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
                quoteCredit.onValueChange(dto.quoteValue.toString())
                creditDate.onValueChange(dto.date)
                kindPayment.onValueChange(Triple(dto.kindOf.ordinal, dto.kindOf.name, dto.kindOf))
                kindRate.onValueChange(Triple(dto.kindOfTax.ordinal, dto.kindOfTax.name, dto.kindOfTax))
            }
        }
    }

    fun backView() {
        navController?.popBackStack()
    }

    fun clean() {
        name.onValueChange("")
        value.onValueChange("")
        rate.onValueChange("")
        month.onValueChange("")
        quoteCredit.onValueChange("")
        creditDate.onValueChange(LocalDate.now())
    }

    fun amortization() {
        // Logic for amortization
    }

    fun calculateQuote() {
        // Logic for calculate quote
    }
}
