package co.com.japl.module.credit.controllers.amortization

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(assistedFactory = AmortizationViewModel.Factory::class)
class AmortizationViewModel @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val code:Int=0,
    @Assisted private val lastDate: LocalDate = LocalDate.now(),
    private val amortizationSvc: IAmortizationTablePort?=null
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(context: Context, savedStateHandle: SavedStateHandle, code: Int, lastDate: LocalDate): AmortizationViewModel
    }
    private val _state = MutableStateFlow(AmortizationState())
    val state = _state.asStateFlow()

    init {
        getAmortization()
    }

    private fun getAmortization() = viewModelScope.launch {
        try {
            _state.value = _state.value.copy(isLoading = true)
            amortizationSvc?.getAmortization(code, KindAmortization.FIXED_QUOTE_SIMULATOR, true)
                ?.let {
                    _state.value = _state.value.copy(
                        amortization = it,
                        isLoading = false
                    )
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }


    data class AmortizationState(
        val amortization: List<AmortizationRowDTO> = emptyList(),
        val isLoading: Boolean = false
    )
}
