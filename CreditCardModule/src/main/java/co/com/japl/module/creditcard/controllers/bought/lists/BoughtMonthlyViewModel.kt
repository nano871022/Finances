package co.com.japl.module.creditcard.controllers.bought.lists

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.navigations.Bought
import co.com.japl.ui.utils.DateUtils
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime

class BoughtMonthlyViewModel constructor(private val creditRate:ITaxPort?,private val creditCardSvc: ICreditCardPort?,private val boughtCreditCardSvc: IBoughtPort?,private val navController: NavController?) : ViewModel(){

    var progress = mutableFloatStateOf(0f)
    var loader = mutableStateOf(false)

    var listCreditCard:List<CreditCardDTO> ? = null
    var creditCardSelected :CreditCardDTO ? = null
    val creditCardList = mutableListOf<Pair<Int,String>>()
    val graphList = mutableListOf<Pair<String,Double>>()
    val creditCard= mutableStateOf("")
    val creditCardCode = mutableIntStateOf(0)

    val cutOff = mutableStateOf(LocalDateTime.now())
    val daysLeftCutOff = mutableIntStateOf(0)
    val totalValue = mutableDoubleStateOf(0.0)

    val capitalValue = mutableDoubleStateOf(0.0)
    val interestValue = mutableDoubleStateOf(0.0)
    val warningValue = mutableDoubleStateOf(0.0)

    val toQuotes = mutableIntStateOf(0)
    val toOneQuote = mutableIntStateOf(0)
    val limitvalue = mutableDoubleStateOf(0.0)

    val lastMonthPaid = mutableStateOf(LocalDateTime.now())
    val totalValueLastMonth = mutableDoubleStateOf(0.0)
    val capitalWithoutQuotesLastMonth = mutableDoubleStateOf(0.0)
    val capitalQuotesLastMonth = mutableDoubleStateOf(0.0)
    val interestValueLastMonth = mutableDoubleStateOf(0.0)

    val showList = mutableStateOf(false)
    val showPeriodList = mutableStateOf(true)
    val showBought = mutableStateOf(false)
    val showAdvance = mutableStateOf(false)
    val showWallet = mutableStateOf(false)

    fun goToPaidList(){
       navController?.let{ Bought.navigate(creditCardSelected?.id!!,creditCardSelected?.cutOffDay!!,cutOff.value,it)}
    }

    fun goToPeriodList(){
        navController?.let{ Bought.navigatePeriodList(creditCardSelected?.id?.toShort()!!,it)}
    }

    fun goToAddBought(){
        navController?.let{ Bought.navigateAddBought(creditCardSelected?.id?.toShort()!!,it)}
    }

    fun goToAddBoughtWallet(){
        navController?.let{ Bought.navigateAddBoughtWallet(creditCardSelected?.id?.toShort()!!,it)}
    }

    fun goToAddAdvance(){
        navController?.let{ Bought.navigateAddAdvance(creditCardSelected?.id?.toShort()!!,it)}
    }

    fun clear(){
        totalValue.value = 0.0
        capitalValue.value = 0.0
        interestValue.value = 0.0
        warningValue.value = 0.0
        toQuotes.value = 0
        toOneQuote.value = 0
        warningValue.value = 0.0
        limitvalue.value = 0.0
        lastMonthPaid.value = LocalDateTime.now()
        totalValueLastMonth.value = 0.0
        capitalWithoutQuotesLastMonth.value = 0.0
        capitalQuotesLastMonth.value = 0.0
        interestValueLastMonth.value = 0.0
        cutOff.value = LocalDateTime.now()
        lastMonthPaid.value = LocalDateTime.now().minusMonths(1)
        graphList.clear()
    }

    fun mainCreditCard()= runBlocking {
        executeCreditCard()
    }

    suspend fun executeCreditCard() {
        creditCardSvc?.getAll()?.let {
            listCreditCard = it
            creditCardList.clear()
            creditCardList.addAll(it.map { Pair<Int, String>(it.id, it.name) })
            if(it.size == 1){
                creditCardCode.intValue = it.first().id
                creditCard.value = it.first().name
                main()
            }
        }
    }

    fun main()= runBlocking {
        loader.value = false
        progress.floatValue = 1f
        execute()
        progress.floatValue = 100f
        loader.value = true
    }
    suspend fun execute(){


        if (creditCardCode.intValue != 0) {
            clear()
            listCreditCard?.first { it.id == creditCardCode.intValue }?.let {creditCard->
                progress.floatValue = 0.3f
                creditCardSelected = creditCard
                DateUtils.cutOff(creditCard.cutOffDay, LocalDate.now())?.let {
                    cutOff.value = it
                }
                progress.floatValue = 0.4f
                DateUtils.cutOffLastMonth(creditCard.cutOffDay, cutOff.value)?.let {
                    lastMonthPaid.value = it
                }
                progress.floatValue = 0.5f
                warningValue.value = creditCard.warningValue.toDouble()
                progress.floatValue = 0.6f
                boughtCreditCardSvc?.getRecap(creditCard, cutOff.value)?.let {
                    it.current?.let {
                        progress.floatValue = 0.7f
                        capitalValue.value = it.capitalValue
                        interestValue.value = it.interestValue
                        toQuotes.value = it.numQuotes
                        toOneQuote.value = it.numOneQuote
                        totalValue.value = it.totalQuote
                    }
                    limitvalue.value += totalValue.value - warningValue.value

                    it.graph?.let(graphList::addAll)

                    it.last?.let {
                        progress.floatValue = 0.8f
                        capitalWithoutQuotesLastMonth.value += it.capitalOneQuote
                        capitalQuotesLastMonth.value += it.capitalQuotes
                        interestValueLastMonth.value += it.interestValue
                        totalValueLastMonth.value += it.totalQuote
                    }
                }

                creditRate?.let {
                    progress.floatValue = 0.9f
                    it.getByCreditCard(creditCard.id)?.let{
                        it.forEach {
                            when(it.kind){
                                KindInterestRateEnum.CASH_ADVANCE->showAdvance.value = true
                                KindInterestRateEnum.WALLET_BUY->showWallet.value = true
                                KindInterestRateEnum.CREDIT_CARD->{
                                    showBought.value = true
                                    showList.value = true
                                }
                            }
                        }
                    }

                }
            }
        }

    }

}