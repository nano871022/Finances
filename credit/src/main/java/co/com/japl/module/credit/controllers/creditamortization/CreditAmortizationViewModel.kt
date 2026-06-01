package co.com.japl.module.credit.controllers.creditamortization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import co.com.japl.module.credit.model.CreditAmortizationState
import co.com.japl.module.credit.navigations.CreditList
import co.com.japl.module.credit.navigations.ExtraValueList
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject

@HiltViewModel
class CreditAmortizationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val creditSvc: ICreditPort?,
    private val additionalSvc: IAdditional?,
    private val gracePeriodSvc: IPeriodGracePort?,
    private val amortizationSvc: IAmortizationTablePort?
): ViewModel() {

    private val creditCode: Int = savedStateHandle.get<Long>("CREDIT_CODE")?.toInt() ?: 0
    private val lastDate: LocalDate = savedStateHandle.get<LocalDate>("LAST_DATE") ?: LocalDate.now()
    lateinit var navController: NavController
        set

    private val _state = MutableStateFlow(CreditAmortizationState())
    val state = _state.asStateFlow()

    init{
        execute()
    }


    fun goToExtraValues() {
        navController?.let{
            ExtraValueList.list(creditCode,it)
        }
    }
    fun goToAdditional() {
        navController?.let{
            CreditList.additional(creditCode,it)
        }
    }


    fun execute()=viewModelScope.launch {
          loadData()
    }

    suspend fun loadData(){
        _state.value = _state.value.copy(isLoading = true)
        val credit = creditSvc?.getCredit(creditCode)
        val additionalList = additionalSvc?.getAdditional(creditCode)
        val gracePeriodList = gracePeriodSvc?.get(creditCode)
        val amortizationTable = amortizationSvc?.getAmortization(creditCode, KindAmortization.FIXED_QUOTE, false)
        val additional = additionalList?.sumOf { it.value }
        val gracePeriods = gracePeriodList?.filter { it.create > lastDate || it.end < lastDate }?.takeIf { it.isNotEmpty() }?.sumOf{it.periods.toInt()}
        val months = ChronoUnit.MONTHS.between(credit?.date, lastDate)
        _state.value = _state.value.copy(
            credit = credit,
            additional = additional,
            gracePeriod = gracePeriods?.toShort(),
            quotesPaid = months.toInt(),
            amortization = amortizationTable,
            isLoading = false
        )
    }

}
