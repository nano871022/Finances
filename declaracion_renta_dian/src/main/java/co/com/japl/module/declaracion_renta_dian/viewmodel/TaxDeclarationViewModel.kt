package co.com.japl.module.declaracion_renta_dian.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.TaxDeclarationDTO
import co.com.japl.finances.iports.dtos.TaxHistoryDTO
import co.com.japl.finances.iports.dtos.TaxProjectionDTO
import co.com.japl.finances.iports.dtos.PatrimonyAssetDTO
import co.com.japl.finances.iports.inbounds.common.GetTaxDeclarationUseCase
import co.com.japl.finances.iports.inbounds.common.GetTaxHistoryUseCase
import co.com.japl.finances.iports.inbounds.common.GetTaxProjectionUseCase
import co.com.japl.finances.iports.inbounds.common.SavePatrimonyAssetUseCase
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
