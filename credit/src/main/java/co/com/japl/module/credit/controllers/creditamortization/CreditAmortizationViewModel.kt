package co.com.japl.module.credit.controllers.creditamortization

import androidx.lifecycle.SavedStateHandle
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
import java.time.LocalDate
import javax.inject.Inject

class CreditAmortizationViewModel @Inject constructor(
    private val creditSvc: ICreditPort,
    private val additionalSvc: IAdditional,
    private val gracePeriodSvc: IPeriodGracePort,
    private val amortizationSvc: IAmortizationTablePort,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(CreditAmortizationState())
    val state = _state.asStateFlow()

    private var creditCode: Int = 0
    private lateinit var lastDate: LocalDate

    init{
        savedStateHandle.get<Int>("creditCode")?.let{
            creditCode = it
        }
        savedStateHandle.get<LocalDate>("lastDate")?.let{
            lastDate = it
        }
        execute()
    }


    fun goToExtraValues(navController: NavController) {
        ExtraValueList.list(creditCode,navController)
    }
    fun goToAdditional(navController: NavController) {
        CreditList.additional(creditCode,navController)
    }


    fun execute()=viewModelScope.launch {
          loadData()
    }

    suspend fun loadData(){
        _state.value = _state.value.copy(isLoading = true)
        val credit = creditSvc.getCredit(creditCode)
        val additionalList = additionalSvc.getAdditional(creditCode)
        val gracePeriodList = gracePeriodSvc.get(creditCode)
        val amortizationTable = amortizationSvc.getAmortization(creditCode, KindAmortization.FIXED_QUOTE, false)
        val additional = additionalList?.sumOf { it.value }
        val gracePeriods = gracePeriodList?.filter { it.create > lastDate || it.end < lastDate }.takeIf { it?.isNotEmpty() == true }?.sumOf{it.periods.toInt()}
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
