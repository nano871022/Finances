package co.com.japl.module.credit.controllers.creditamortization

import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.domain.Credit
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.GracePeriodDTO
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort

@HiltViewModel
class CreditAmortizationViewModel @Inject constructor(
    private val creditSvc: ICreditPort,
    private val additionalSvc: IAdditional,
    private val gracePeriodSvc: IPeriodGracePort,
    private val amortizationSvc: IAmortizationTablePort
): ViewModel() {

    private val _state = MutableStateFlow(CreditAmortizationState())
    val state = _state.asStateFlow()

    fun loadData(creditCode:Int, lastDate:LocalDate){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val credit = creditSvc.get(creditCode)
            val additionalList = additionalSvc.get(creditCode)
            val gracePeriodList = gracePeriodSvc.get(creditCode)
            val amortizationTable = amortizationSvc.getAmortization(creditCode, KindAmortization.CREDIT_FIX, false)
            val additional = additionalList.map { it.value }.reduceOrNull { acc, bigDecimal -> acc + bigDecimal }
                ?: BigDecimal.ZERO
            val gracePeriods = gracePeriodList.filter { it.create > lastDate || it.end < lastDate }.takeIf { it.isNotEmpty() }?.map{it.periods}?.reduceOrNull{a,b->a+b}?:0
            val months = ChronoUnit.MONTHS.between(credit.get().date, lastDate)
            _state.value = _state.value.copy(
                credit = credit.get(),
                additional = additional,
                gracePeriod = gracePeriods.toShort(),
                quotesPaid = months.toInt(),
                amortization = amortizationTable,
                isLoading = false
            )
        }
    }

}

data class CreditAmortizationState(
    val credit:CreditDTO? = null,
    val additional:BigDecimal? = null,
    val gracePeriod:Short? = null,
    val quotesPaid:Int? = null,
    val amortization:List<AmortizationRowDTO>? = null,
    val isLoading:Boolean = false
)
