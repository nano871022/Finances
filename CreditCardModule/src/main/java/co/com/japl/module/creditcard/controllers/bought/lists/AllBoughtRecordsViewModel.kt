package co.com.japl.module.creditcard.controllers.bought.lists

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllBoughtRecordsViewModel(
    private val boughtCreditCardSvc: IBoughtListPort,
    private val creditCardSvc: ICreditCardPort
) : ViewModel() {

    private val _list = mutableListOf<CreditCardBoughtItemDTO>()
    val list = mutableStateOf<List<CreditCardBoughtItemDTO>>(emptyList())
    val filteredList = mutableStateOf<List<CreditCardBoughtItemDTO>>(emptyList())
    val loader = mutableStateOf(false)
    val searchQuery = mutableStateOf("")
    val showOnlyRestorable = mutableStateOf(false)

    val banksList = mutableStateOf<List<Pair<Int, String>>>(emptyList())
    val selectedBank = mutableStateOf<Pair<Int, String>?>(null)

    init {
        loadBanks()
    }

    fun loadBanks() = viewModelScope.launch {
        val cards = withContext(Dispatchers.IO) {
            creditCardSvc.getAll()
        }
        banksList.value = cards.map { Pair(it.id, it.name) }
    }

    fun onBankSelected(bank: Pair<Int, String>?) {
        selectedBank.value = bank
        if (bank != null) {
            load(bank.first)
        } else {
            _list.clear()
            list.value = emptyList()
            filteredList.value = emptyList()
            loader.value = false
        }
    }

    fun load(bankId: Int) = viewModelScope.launch {
        loader.value = true
        val items = withContext(Dispatchers.IO) {
            boughtCreditCardSvc.getAllBoughts()
        }
        val filteredByBank = items.filter { it.codeCreditCard == bankId }
        _list.clear()
        _list.addAll(filteredByBank)
        list.value = _list.toList()
        applyFilter()
        loader.value = false
    }

    fun restore(item: CreditCardBoughtItemDTO) = viewModelScope.launch {
        loader.value = true
        val success = withContext(Dispatchers.IO) {
            boughtCreditCardSvc.restoreEndDate(item.id, false)
        }
        if (success) {
            selectedBank.value?.let { load(it.first) }
        } else {
            loader.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        applyFilter()
    }

    fun onShowOnlyRestorableChange(value: Boolean) {
        showOnlyRestorable.value = value
        applyFilter()
    }

    private fun applyFilter() {
        val query = searchQuery.value.trim().lowercase()
        var result = _list.toList()

        if (showOnlyRestorable.value) {
            result = result.filter { it.restorable }
        }

        if (query.isNotBlank()) {
            result = result.filter {
                it.nameItem.lowercase().contains(query) ||
                        it.nameCreditCard.lowercase().contains(query)
            }
        }
        filteredList.value = result
    }
}
