package co.com.japl.module.creditcard.controllers.bought.lists

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.ui.utils.NumbersUtil
import dagger.hilt.android.lifecycle.HiltViewModel

import java.time.YearMonth
import javax.inject.Inject


class BoughtMonthlyListViewModel (
    private var _key:YearMonth,
    private var _list:List<CreditCardBoughtItemDTO>,
    private var _totalBought:String,
    private var _totalQuote:String,
    private var _totalInterest:String
): ViewModel() {

    val key:YearMonth get() = _key
    val list = _list.toMutableStateList()

    val totalBought:String get() = _totalBought

    val totalQuote:String get() = _totalQuote

    val totalInterest:String get() = _totalInterest

    val totalBoughtValue:Double get() = _list.sumOf { it.pendingToPay }
    val totalQuoteValue:Double get() = _list.sumOf { it.quoteValue }

    fun compactTotalBought():String = NumbersUtil.formatReduced(totalBoughtValue)
    fun compactTotalQuote():String = NumbersUtil.formatReduced(totalQuoteValue)

    var state: MutableState<Boolean> = mutableStateOf(false)

}