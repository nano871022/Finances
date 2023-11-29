package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import javax.inject.Inject

//@HiltViewModel
class BoughtMonthlyViewModel @Inject constructor(
    private var _key:YearMonth,
    private var _list:List<CreditCardBoughtItemDTO>,
    private var _totalBought:String,
    private var _totalQuote:String
): ViewModel() {

    val key:YearMonth get() = _key
    val list:List<CreditCardBoughtItemDTO> get() = _list

    val totalBought:String get() = _totalBought

    val totalQuote:String get() = _totalQuote

    var state: MutableState<Boolean> = mutableStateOf(false)

}