package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.common.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.pojo.CreditCard
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Optional
import javax.inject.Inject

class ListBoughtViewModel @Inject constructor(
    private  var _taxSvc: ITaxSvc,
    private  var _boughtListSvc: IBoughtListPort,
    private  var _creditCardSvc: ICreditCardPort,
    private var _differInstallmentSvc: IDifferQuotesPort,
    private var navController: NavController
) : ViewModel() {

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
        Log.d(javaClass.name,"go to cash advance")
        CashAdvanceParams.newInstanceFloat(_creditCard.codeCreditCard.get()
            ,navController)
    }

    fun goToCreditCard(){
        Log.d(javaClass.name,"go to credit card")
        CreditCardQuotesParams.Companion.ListBought.newInstanceFloat(0
            ,_creditCard.codeCreditCard.get()
            ,navController)
    }

    fun main() = runBlocking {
        getQuotes()
    }

    suspend fun getQuotes(){
        progress.floatValue = 0.1f
        _taxSvc.get(_params.first.toLong(),_params.second.monthValue,_params.second.year,
            TaxEnum.CASH_ADVANCE).takeIf { it.isPresent }?.get()?.let {
            cashAdvance.value = true
        }
        progress.floatValue = 0.2f
        _taxSvc.get(_params.first.toLong(),_params.second.monthValue,_params.second.year,
            TaxEnum.CREDIT_CARD).takeIf { it.isPresent }?.get()?.let {
            creditCard.value = true
        }
        progress.floatValue = 0.3f
        val creditCardDto = _creditCardSvc.getCreditCard(_creditCard.codeCreditCard.get())
        progress.floatValue = 0.4f
        val differ = _differInstallmentSvc.getDifferQuote(_creditCard.cutOff.get().toLocalDate())
        progress.floatValue = 0.5f
        val bought = _boughtListSvc.getBoughtList(creditCardDto!!,_creditCard.cutOff.get())
        progress.floatValue = 0.8f
        val group = bought.list?.sortedByDescending { it.boughtDate!! }?.groupBy { YearMonth.of(it.boughtDate.year,it.boughtDate.monthValue)!! }!!
        progress.floatValue = 0.9f
        _boughtCreditCard= BoughtCreditCard(bought.recap,group,creditCardDto,differ, cutOff = _creditCard.cutOff.get())
        progress.floatValue = 1f
        isLoad.value = true
    }
}