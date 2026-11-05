package co.com.japl.module.credit.controllers.amortization

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import co.com.japl.module.credit.params.AmortizationTableParams

@HiltViewModel
class AmortizationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val amortizationSvc: IAmortizationTablePort?
) : ViewModel() {

    private val params: Map<String, Any> by lazy { AmortizationTableParams.download(savedStateHandle) }
    private val code: Int get() = (params["CODE"] as? Long)?.toInt() ?: 0
    private val lastDate: LocalDate get() = params["LAST_DATE"] as? LocalDate ?: LocalDate.now()

    private val _state = MutableStateFlow(AmortizationState())
    val state = _state.asStateFlow()

    init {
        getAmortization()
    }

    fun getAmortization() = viewModelScope.launch {
        try {
            _state.value = _state.value.copy(isLoading = true)
            val result = withContext(Dispatchers.IO) {
                amortizationSvc?.getAmortization(code, KindAmortization.FIXED_QUOTE_SIMULATOR, false)
            }
            _state.value = _state.value.copy(
                amortization = result ?: emptyList(),
                isLoading = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false)
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }


    data class AmortizationState(
        val amortization: List<AmortizationRowDTO> = emptyList(),
        val isLoading: Boolean = false
    )
}
