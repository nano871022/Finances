package co.com.japl.module.credit.controllers.extravalue

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            withContext(Dispatchers.IO) {
                extraValueAmortizationCreditSvc?.getAll(creditId)
            }?.let{
                _extraValues.value = it
            }
    }

    fun create( numQuotes: Int, value: Double)= viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                extraValueAmortizationCreditSvc?.save(id, numQuotes.toLong(), value) ?: 0
            }
            if(result > 0){
                exec()
            }
        }

}