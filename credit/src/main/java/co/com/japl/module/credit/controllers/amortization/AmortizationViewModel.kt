package co.com.japl.module.credit.controllers.amortization

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ViewModelScoped
class AmortizationViewModel constructor(
    private val savedStateHandle: SavedStateHandle?=null,
    private val code:Int=0,
    private val amortizationSvc: IAmortizationTablePort?=null
) : ViewModel() {
    private val _state = MutableStateFlow(AmortizationState())
    val state = _state.asStateFlow()

    init {
        getAmortization()
    }

    private fun getAmortization() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            amortizationSvc?.getAmortization(code, KindAmortization.FIXED_QUOTE_SIMULATOR, true)?.let {
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
