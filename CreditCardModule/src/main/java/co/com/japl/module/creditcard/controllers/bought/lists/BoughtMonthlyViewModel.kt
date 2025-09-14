package co.com.japl.module.creditcard.controllers.bought.lists

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.navigations.Bought
import co.com.japl.module.creditcard.navigations.ListCreditRate
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

import androidx.lifecycle.SavedStateHandle

class BoughtMonthlyViewModel constructor(
                                        private val creditRate:ITaxPort?,
                                        private val creditCardSvc: ICreditCardPort?,
                                        private val boughtCreditCardSvc: IBoughtPort?,
                                        private val prefs:Prefs,
                                        private val msmSvc: ISMSCreditCardPort?,
                                        private val svc: IBoughtSmsPort?,
                                        private val savedStateHandle: SavedStateHandle?
                                        ) : ViewModel(){

    val cache = mutableStateOf(prefs.simulator)
    var progress = mutableFloatStateOf(0f)
    var loader = mutableStateOf(false)

    var listCreditCard:List<CreditCardDTO> ? = null
    var creditCardSelected :CreditCardDTO ? = null
    val creditCardList = mutableListOf<Pair<Int,String>>()
    val graphList = mutableListOf<Pair<String,Double>>()
    val graphListPeriod = mutableListOf<Pair<String,Double>>()
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
    val showCreditRate = mutableStateOf(false)

    fun goToPaidList(navController: NavController){
       Bought.navigate(creditCardSelected?.id!!,creditCardSelected?.cutOffDay!!,cutOff.value,navController)
    }

    fun goToPeriodList(navController: NavController){
        Bought.navigatePeriodList(creditCardSelected?.id?.toShort()!!,navController)
    }

    fun goToAddBought(navController: NavController){
        Bought.navigateAddBought(creditCardSelected?.id?.toShort()!!,navController)
    }

    fun goToAddBoughtWallet(navController: NavController){
        Bought.navigateAddBoughtWallet(creditCardSelected?.id?.toShort()!!,navController)
    }

    fun goToAddAdvance(navController: NavController){
        Bought.navigateAddAdvance(creditCardSelected?.id?.toShort()!!,navController)
    }

    fun goToAddCreditRate(navController: NavController){
        ListCreditRate.navigate_list(navController)
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
        showBought.value = false
        showPeriodList.value = true
        showList.value = false
        showAdvance.value = false
        showWallet.value = false
        showCreditRate.value = false
    }

    fun mainCreditCard()= runBlocking {
        executeCreditCard()
    }

    suspend fun executeCreditCard() {
        creditCardSvc?.getAll()?.let {
            listCreditCard = it
            creditCardList.clear()
            creditCardList.addAll(it.map { Pair<Int, String>(it.id, it.name) })
            if(it.size == 1 && creditCard.value.isBlank()){
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
        readSms()
    }


    suspend fun readSms(){
        try {
            listCreditCard?.takeIf { it.isNotEmpty() }?.forEach { ccDto ->
                arrayListOf(
                msmSvc?.getByCreditCardAndKindInterest(ccDto.id, KindInterestRateEnum.CREDIT_CARD)
                ,msmSvc?.getByCreditCardAndKindInterest(ccDto.id, KindInterestRateEnum.CASH_ADVANCE)
                ,msmSvc?.getByCreditCardAndKindInterest(ccDto.id, KindInterestRateEnum.WALLET_BUY)
                    )?.flatMap{ it!! }
                    ?.forEach { sms ->
                        msmSvc?.getSmsMessages(sms.phoneNumber, sms.pattern,prefs.creditCardSMSDaysRead)
                            .takeIf { it?.isNotEmpty() == true }
                            ?.forEach {
                                svc?.createBySms(
                                    name = it.first,
                                    value = it.second,
                                    date = it.third,
                                    codeCreditRate = ccDto.id,
                                    kind = sms.kindInterestRateEnum
                                )
                            }
                    }
            }
        }catch (e:Exception){
            Log.e(javaClass.name,e.message,e)
        }
    }

    suspend fun execute(){
        if (creditCardCode.intValue != 0) {
            clear()
            listCreditCard?.first { it.id == creditCardCode.intValue }?.let {creditCard->
                progress.floatValue = 0.2f
                creditCardSelected = creditCard
                DateUtils.cutOff(creditCard.cutOffDay, LocalDate.now())?.let {
                    cutOff.value = it
                }
                daysLeftCutOff.value = Period.between(LocalDate.now(),cutOff.value.toLocalDate()).days
                progress.floatValue = 0.3f
                DateUtils.cutOffLastMonth(creditCard.cutOffDay, cutOff.value)?.let {
                    lastMonthPaid.value = it
                }
                progress.floatValue = 0.4f
                warningValue.value = creditCard.warningValue.toDouble()
                progress.floatValue = 0.5f
                boughtCreditCardSvc?.getRecap(creditCard, cutOff.value,cache.value)?.let {
                    it.current?.let {
                        progress.floatValue = 0.6f
                        capitalValue.value = it.capitalValue
                        interestValue.value = it.interestValue
                        toQuotes.value = it.numQuotes
                        toOneQuote.value = it.numOneQuote
                        totalValue.value = it.totalQuote
                    }
                    limitvalue.value += warningValue.value - totalValue.value

                    it.graph?.let(graphList::addAll)

                    it.last?.let {
                        progress.floatValue = 0.7f
                        capitalWithoutQuotesLastMonth.value += it.capitalOneQuote
                        capitalQuotesLastMonth.value += it.capitalQuotes
                        interestValueLastMonth.value += it.interestValue
                        totalValueLastMonth.value += it.totalQuote
                    }
                }

                boughtCreditCardSvc?.getBoughtCurrentPeriodList(creditCard,cutOff.value,cache.value)?.let{
                    graphListPeriod.addAll(it)
                    progress.floatValue = 0.8f
                }

                creditRate?.let {

                    it.get(creditCard.id,cutOff.value.monthValue,cutOff.value.year,KindInterestRateEnum.CREDIT_CARD)?.let{
                        showBought.value = true
                        showList.value = true
                        progress.floatValue = 0.85f
                    }

                    it.get(creditCard.id,cutOff.value.monthValue,cutOff.value.year,KindInterestRateEnum.CASH_ADVANCE)?.let{
                        showAdvance.value = true
                        progress.floatValue = 0.9f
                    }

                    it.get(creditCard.id,cutOff.value.monthValue,cutOff.value.year,KindInterestRateEnum.WALLET_BUY)?.let{
                        showWallet.value = true
                        progress.floatValue = 0.95f
                    }
                    if(showWallet.value.not() and showAdvance.value.not() and showBought.value.not()){
                        showCreditRate.value = true
                    }
                    progress.floatValue = 100f
                }
            }
        }

    }

}