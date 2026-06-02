package co.com.japl.module.dian.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.dian.Form210DTO
import co.com.japl.finances.iports.inbounds.dian.IGetTaxDeclarationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaxDeclarationViewModel @Inject constructor(
    private val getTaxDeclarationUseCase: IGetTaxDeclarationUseCase
) : ViewModel() {

    private val _declarationState = MutableStateFlow<Form210DTO?>(null)
    val declarationState: StateFlow<Form210DTO?> = _declarationState

    private val _showCalculatedOnly = MutableStateFlow(false)
    val showCalculatedOnly: StateFlow<Boolean> = _showCalculatedOnly

    init {
        loadData()
    }

    fun loadData() {
        val currentYear = LocalDate.now().year
        viewModelScope.launch {
            _declarationState.value = withContext(Dispatchers.IO) {
                getTaxDeclarationUseCase.getTaxDeclaration(currentYear)
            }
        }
    }

    fun toggleShowCalculatedOnly(value:Boolean) {
        _showCalculatedOnly.value = value
    }
}
