package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.app.Application
import android.content.Context
import android.util.Log
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
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import co.japl.android.myapplication.finanzas.pojo.BoughtCreditCard
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.pojo.CreditCard
import co.japl.finances.core.utils.DateUtils
import dagger.hilt.EntryPoints
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Optional
import javax.inject.Inject

class ListBoughtViewModel @Inject constructor(
    private val application: Application,
    private var navController: NavController?,
    public val prefs:Prefs
) : ViewModel() {

    private  var _taxSvc: ITaxPort = EntryPoints.get(application, EntryPoint::class.java).getInboundTaxPort()
    private  var _boughtListSvc: IBoughtListPort = EntryPoints.get(application, EntryPoint::class.java).getBoughtCreditCardSvc()
    private  var _creditCardSvc: ICreditCardPort = EntryPoints.get(application, EntryPoint::class.java).getCreditCardSvc()
    private var _differInstallmentSvc: IDifferQuotesPort = EntryPoints.get(application, EntryPoint::class.java).getDifferInstallmentSvc()

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