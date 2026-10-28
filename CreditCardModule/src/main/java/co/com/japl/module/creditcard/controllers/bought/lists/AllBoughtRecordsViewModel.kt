package co.com.japl.module.creditcard.controllers.bought.lists

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllBoughtRecordsViewModel(
    private val boughtCreditCardSvc: IBoughtListPort
) : ViewModel() {

    val list = mutableStateListOf<CreditCardBoughtItemDTO>()
    val filteredList = mutableStateListOf<CreditCardBoughtItemDTO>()
    val loader = mutableStateOf(true)
    val searchQuery = mutableStateOf("")

    fun load() = viewModelScope.launch {
        loader.value = true
        val items = withContext(Dispatchers.IO) {
            boughtCreditCardSvc.getAllBoughts()
        }
        list.clear()
        list.addAll(items)
        applyFilter()
        loader.value = false
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        applyFilter()
    }

    private fun applyFilter() {
        filteredList.clear()
        val query = searchQuery.value.trim().lowercase()
        if (query.isBlank()) {
            filteredList.addAll(list)
        } else {
            filteredList.addAll(list.filter {
                it.nameItem.lowercase().contains(query) ||
                        it.nameCreditCard.lowercase().contains(query)
            })
        }
    }
}
