package co.com.japl.module.credit.controllers.extravalue

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class ExtraValueListViewModel @Inject constructor(
    private val id:Int,
    private val extraValueAmortizationCreditSvc: IExtraValueAmortizationCreditPort?=null
) : ViewModel() {

    val _extraValues = MutableStateFlow<List<ExtraValueAmortizationDTO>>(emptyList())
    val extraValues: StateFlow<List<ExtraValueAmortizationDTO>> = _extraValues
    private val isLoading = mutableStateOf(false)

    init{
        exec()
    }

    private fun exec() = viewModelScope.launch{
        isLoading.value = true
        loadExtraValues(id)
        isLoading.value = false
    }

    private suspend fun loadExtraValues(creditId: Int) {
            extraValueAmortizationCreditSvc?.getAll(creditId)?.let{
                _extraValues.value = it
            }
    }

    fun create( numQuotes: Int, value: Double)= viewModelScope.launch {
            if((extraValueAmortizationCreditSvc?.save(id, numQuotes.toLong(), value) ?: 0) > 0){
                exec()
            }
        }

}