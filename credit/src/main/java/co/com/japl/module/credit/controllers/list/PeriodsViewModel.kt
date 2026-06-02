package co.com.japl.module.credit.controllers.list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort
import co.com.japl.ui.utils.FormUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PeriodsViewModel @Inject constructor(private val periodSvc: IPeriodCreditPort?): ViewModel(){
    val records  = mutableStateListOf<PeriodCreditDTO>()
    private val _uiState = MutableStateFlow<FormUIState>(FormUIState.Loading)
    val viewState: StateFlow<FormUIState> = _uiState.asStateFlow()


    init{
        viewModelScope.launch {
            main()
        }
    }

    fun main() {
        viewModelScope.launch {
            execute()
        }
    }

    suspend fun execute(){

        withContext(Dispatchers.IO) {
            periodSvc?.getRecords()
        }?.let(records::addAll)

        _uiState.value = FormUIState.Current

    }

}