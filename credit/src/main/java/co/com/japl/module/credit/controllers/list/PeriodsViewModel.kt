package co.com.japl.module.credit.controllers.list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.PeriodCreditDTO
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort
import co.com.japl.ui.utils.FormUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    fun main()= runBlocking {
        execute()
    }

    suspend fun execute(){

        periodSvc?.getRecords()?.let(records::addAll)

        _uiState.value = FormUIState.Current

    }

}