package com.nano871022.finances.features.declaracion_renta_dian.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nano871022.finances.iport.dto.TaxDeclarationDTO
import com.nano871022.finances.iport.dto.TaxHistoryDTO
import com.nano871022.finances.iport.dto.TaxProjectionDTO
import com.nano871022.finances.iport.dto.PatrimonyAssetDTO
import com.nano871022.finances.iport.ports.inbound.GetTaxDeclarationUseCase
import com.nano871022.finances.iport.ports.inbound.GetTaxHistoryUseCase
import com.nano871022.finances.iport.ports.inbound.GetTaxProjectionUseCase
import com.nano871022.finances.iport.ports.inbound.SavePatrimonyAssetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaxDeclarationViewModel @Inject constructor(
    private val getTaxDeclarationUseCase: GetTaxDeclarationUseCase,
    private val getTaxHistoryUseCase: GetTaxHistoryUseCase,
    private val getTaxProjectionUseCase: GetTaxProjectionUseCase,
    private val savePatrimonyAssetUseCase: SavePatrimonyAssetUseCase
) : ViewModel() {

    private val _declarationState = MutableStateFlow<TaxDeclarationDTO?>(null)
    val declarationState: StateFlow<TaxDeclarationDTO?> = _declarationState

    private val _historyState = MutableStateFlow<List<TaxHistoryDTO>>(emptyList())
    val historyState: StateFlow<List<TaxHistoryDTO>> = _historyState

    private val _projectionState = MutableStateFlow<List<TaxProjectionDTO>>(emptyList())
    val projectionState: StateFlow<List<TaxProjectionDTO>> = _projectionState

    private val _assetsState = MutableStateFlow<List<PatrimonyAssetDTO>>(emptyList())
    val assetsState: StateFlow<List<PatrimonyAssetDTO>> = _assetsState

    init {
        loadData()
    }

    fun loadData() {
        val currentYear = LocalDate.now().year
        viewModelScope.launch {
            _declarationState.value = getTaxDeclarationUseCase.getTaxDeclaration(currentYear - 1)
            _historyState.value = getTaxHistoryUseCase.getTaxHistory()
            _projectionState.value = getTaxProjectionUseCase.getProjection(currentYear)
            _assetsState.value = savePatrimonyAssetUseCase.getAssets()
        }
    }

    fun addAsset(name: String, value: BigDecimal, type: String) {
        viewModelScope.launch {
            savePatrimonyAssetUseCase.saveAsset(PatrimonyAssetDTO(null, name, value, type))
            loadData()
        }
    }

    fun deleteAsset(id: Long) {
        viewModelScope.launch {
            savePatrimonyAssetUseCase.deleteAsset(id)
            loadData()
        }
    }
}
