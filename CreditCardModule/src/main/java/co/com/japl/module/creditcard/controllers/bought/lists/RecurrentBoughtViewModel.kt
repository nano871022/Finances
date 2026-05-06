package co.com.japl.module.creditcard.controllers.bought.lists

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.Prefs
import java.time.LocalDateTime
import co.com.japl.module.creditcard.enums.MoreOptionsRecurrentBought
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.YearMonth

class RecurrentBoughtViewModel(
    private val boughtCreditCardSvc: IBoughtListPort,
    private val prefs: Prefs
) : ViewModel() {

    val list = mutableStateListOf<CreditCardBoughtItemDTO>()
    val filteredList = mutableStateListOf<CreditCardBoughtItemDTO>()
    val totalActive = mutableStateOf("$ 0.00")
    val loader = mutableStateOf(false)
    val filterActive = mutableStateOf<Boolean?>(null) // null: All, true: Active, false: Inactive

    private var codeCreditCard: Int = 0
    private lateinit var navController: NavController

    fun load(codeCreditCard: Int, navController: NavController) {
        this.codeCreditCard = codeCreditCard
        this.navController = navController
        loader.value = false // Loading
        val result = boughtCreditCardSvc.getAllRecurrent(codeCreditCard)
        list.clear()
        list.addAll(result)
        applyFilter()
        loader.value = true // Loaded
    }

    fun applyFilter() {
        filteredList.clear()
        val filtered = when (filterActive.value) {
            true -> list.filter { it.endDate == LocalDateTime.MAX || it.endDate.isAfter(LocalDateTime.now()) }
            false -> list.filter { it.endDate != LocalDateTime.MAX && it.endDate.isBefore(LocalDateTime.now()) }
            else -> list
        }
        filteredList.addAll(filtered.sortedByDescending { it.createDate })

        val total = list.filter { it.endDate == LocalDateTime.MAX || it.endDate.isAfter(LocalDateTime.now()) }
            .sumOf { it.valueItem }
        totalActive.value = NumbersUtil.COPtoString(total)
    }

    fun setFilter(active: Boolean?) {
        filterActive.value = active
        applyFilter()
    }

    fun handleAction(item: CreditCardBoughtItemDTO, action: MoreOptionsRecurrentBought) {
        when (action) {
            MoreOptionsRecurrentBought.ACTIVATE -> reactivate(item)
            MoreOptionsRecurrentBought.DEACTIVATE -> deactivate(item)
            MoreOptionsRecurrentBought.DELETE -> delete(item)
            MoreOptionsRecurrentBought.COPY -> clone(item)
            MoreOptionsRecurrentBought.ALTER -> alter(item)
            MoreOptionsRecurrentBought.EDIT -> edit(item)
        }
    }

    private fun edit(item: CreditCardBoughtItemDTO) {
        // This will need to be handled by the UI/Fragment to navigate correctly
    }

    private fun reactivate(item: CreditCardBoughtItemDTO) {
        if (boughtCreditCardSvc.reactivateRecurrent(item.id)) {
            load(codeCreditCard, navController)
        }
    }

    private fun deactivate(item: CreditCardBoughtItemDTO) {
        if (boughtCreditCardSvc.endingRecurrentPayment(item.id, LocalDateTime.now())) {
            load(codeCreditCard, navController)
        }
    }

    private fun delete(item: CreditCardBoughtItemDTO) {
        if (boughtCreditCardSvc.delete(item.id, prefs.simulator)) {
            load(codeCreditCard, navController)
        }
    }

    private fun clone(item: CreditCardBoughtItemDTO) {
        if (boughtCreditCardSvc.clone(item.id, prefs.simulator)) {
            load(codeCreditCard, navController)
        }
    }

    private fun alter(item: CreditCardBoughtItemDTO) {
        // Navigation should be handled by the UI
    }
}
