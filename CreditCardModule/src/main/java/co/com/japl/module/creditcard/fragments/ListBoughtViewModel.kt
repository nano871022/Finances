package co.com.japl.module.creditcard.fragments

import android.app.Application
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.Prefs
import co.com.japl.finances.iports.pojo.BoughtCreditCard
import co.com.japl.module.creditcard.params.CashAdvanceParams
import co.com.japl.module.creditcard.params.CreditCardQuotesParams
import co.japl.android.myapplication.pojo.CreditCard
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Optional
import javax.inject.Inject

class ListBoughtViewModel @Inject constructor(
    private val application: Application,
    private var navController: NavController?,
    val prefs:Prefs,
    private val taxSvc: ITaxPort,
    private val boughtListSvc: IBoughtListPort,
    private val creditCardSvc: ICreditCardPort,
    private val differInstallmentSvc: IDifferQuotesPort
) : ViewModel() {

    private  var _taxSvc: ITaxPort = taxSvc
    private  var _boughtListSvc: IBoughtListPort = boughtListSvc
    private  var _creditCardSvc: ICreditCardPort = creditCardSvc
    private var _differInstallmentSvc: IDifferQuotesPort = differInstallmentSvc

    val cache = mutableStateOf(prefs.simulator)
    val cashAdvance = mutableStateOf(false)
    val creditCard = mutableStateOf(false)
    private lateinit var _boughtCreditCard:BoughtCreditCard
    val boughtCreditCard get() = _boughtCreditCard

    private lateinit var _params: Triple<Int, LocalDateTime, Short>
    private lateinit var _creditCard:CreditCard

    var progress = mutableFloatStateOf(0f)
    var isLoad = mutableStateOf(false)

    fun setParams(params: Triple<Int, LocalDateTime, Short>?){
        params?.let{ param ->
            _params = param
            _creditCard = CreditCard()
            _creditCard.codeCreditCard = Optional.ofNullable(param.first)
            _creditCard.cutOff = Optional.ofNullable(param.second)
            _creditCard.cutoffDay = Optional.ofNullable(param.third)
        }
    }

    fun goToCashAdvance(){
        navController?.let {
            CashAdvanceParams.newInstanceFloat(
                _creditCard.codeCreditCard.get(), it
            )
        }
    }

    fun goToCreditCard(){
        navController?.let {
            CreditCardQuotesParams.Companion.ListBought.newInstanceFloat(
                0, _creditCard.codeCreditCard.get(), it
            )
        }
    }

    fun main() = runBlocking {
        getQuotes()
    }

    suspend fun getQuotes(){
        progress.floatValue = 0.1f
        _taxSvc.get(_params.first,_params.second.monthValue,_params.second.year,
            KindInterestRateEnum.CASH_ADVANCE)?.let {
            cashAdvance.value = true
        }
        progress.floatValue = 0.2f
        _taxSvc.get(_params.first,_params.second.monthValue,_params.second.year,
            KindInterestRateEnum.CREDIT_CARD)?.let {
            creditCard.value = true
        }
        progress.floatValue = 0.3f
        val creditCardDto = _creditCardSvc.getCreditCard(_creditCard.codeCreditCard.get())
        progress.floatValue = 0.4f
        val differ = _differInstallmentSvc.getDifferQuote(_creditCard.cutOff.get().toLocalDate())
        progress.floatValue = 0.5f
        val bought = _boughtListSvc.getBoughtList(creditCardDto!!,_creditCard.cutOff.get(),cache.value)
        progress.floatValue = 0.8f
        val group = bought.list.takeIf { it.isNotEmpty() }
            ?.sortedByDescending { it.boughtDate }
            ?.groupBy {
                YearMonth.from(it.boughtDate)
        }?: emptyMap()
        progress.floatValue = 0.9f
        _boughtCreditCard= BoughtCreditCard(bought.recap,group,creditCardDto,differ, cutOff = _creditCard.cutOff.get())
        progress.floatValue = 1f
        isLoad.value = true
    }
}
