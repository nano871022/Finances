package co.com.japl.module.credit.controllers.amortization

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class AmortizationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val amortizationSvc: IAmortizationTablePort
) : ViewModel() {
    private val _state = MutableStateFlow(AmortizationState())
    val state = _state.asStateFlow()
    private var code:Int=0
    private var lastDate: LocalDate = LocalDate.now()


    init {
        savedStateHandle.get<Int>("code")?.let{ code = it }
        savedStateHandle.get<LocalDate>("lastDate")?.let{ lastDate = it }
        getAmortization()
    }

    private fun getAmortization() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            amortizationSvc.getAmortization(code, KindAmortization.FIXED_QUOTE_SIMULATOR, true).let {
                _state.value = _state.value.copy(
                    amortization = it,
                    isLoading = false
                )
            }
        }
    }
}

data class AmortizationState(
    val amortization: List<AmortizationRowDTO> = emptyList(),
    val isLoading: Boolean = false
)
