package co.com.japl.credit.controller.amortization

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmortizationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val amortizationSvc: ISimulatorCreditFixPort
) : ViewModel() {
    private val _state = MutableStateFlow(AmortizationState())
    val state = _state.asStateFlow()
    private val id: Int = savedStateHandle.get<Int>("id") ?: 0

    init {
        getAmortization()
    }

    private fun getAmortization() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val amortization = amortizationSvc.getAmortization(id, KindAmortization.FIX_QUOTE_SIMULATOR, true)
            _state.value = _state.value.copy(
                amortization = amortization,
                isLoading = false
            )
        }
    }
}

data class AmortizationState(
    val amortization: List<AmortizationRowDTO> = emptyList(),
    val isLoading: Boolean = false
)
