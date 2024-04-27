package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import android.app.Application
import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.mapping.CalcMap
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCard
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.com.japl.ui.utils.DateUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoints
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BoughtViewModel @Inject constructor(
    private val application:Application,
    private val prefs:Prefs,
    private val loader: MutableState<Boolean> = mutableStateOf(false)
    ):AndroidViewModel(application) {

        val cache = mutableStateOf(prefs.simulator)

    private val boughtCreditCardSvc:IBoughtListPort = EntryPoints.get(application, EntryPoint::class.java).getBoughtCreditCardSvc()
    private lateinit var _bought:CreditCardBoughtItemDTO
    private lateinit var _differQuotes:List<DifferInstallmentDTO>
    private lateinit var _creditCard:CreditCardDTO
    private lateinit var _cutOff:LocalDateTime
    private lateinit var navController: NavController
    private lateinit var view: View
    val bought:CreditCardBoughtItemDTO get() = _bought

    fun setCutOff(cutOff:LocalDateTime):BoughtViewModel {
        _cutOff = cutOff
        return this
    }
    fun setBought(bought:CreditCardBoughtItemDTO): BoughtViewModel {
        _bought = bought
        return this
    }

    fun setNavController(navController: NavController):BoughtViewModel {
        this.navController = navController
        return this
    }

    fun setView(view: View):BoughtViewModel {
        this.view = view
        return this
    }

    fun setDifferQuotes(differQuotes:List<DifferInstallmentDTO>):BoughtViewModel {
        _differQuotes = differQuotes
        return this
    }

    fun setCreditCard(creditCard:CreditCardDTO):BoughtViewModel {
        _creditCard = creditCard
        return this
    }

    fun moreOption(option:MoreOptionsItemsCreditCard,value:Double=0.0){
        when(option){
            MoreOptionsItemsCreditCard.EDIT ->{goToEditBought()}
            MoreOptionsItemsCreditCard.ENDING ->{endingDialog()}
            MoreOptionsItemsCreditCard.UPDATE_VALUE ->{updateRecurrentValueDialog(value)}
            MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT ->{differntInstallmentDialog(value.toLong())}
            MoreOptionsItemsCreditCard.AMORTIZATION ->{goToAmortization()}
            MoreOptionsItemsCreditCard.DELETE ->{deleteDialog()}
            MoreOptionsItemsCreditCard.CLONE ->{clone()}
            MoreOptionsItemsCreditCard.RESTORE ->{restoreDialog()}
        }
    }

    private fun restoreDialog(){
        if(bought.id > 0 && boughtCreditCardSvc.restore(bought.id,cache.value)){
            Snackbar.make(
                view,
                R.string.ending_recurrent_restore_payment_successfull,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close) {}
                .show().also { loader.value = false }
        }else{
            Snackbar.make(
                view,
                R.string.dont_restore_payment_successfull,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close, null).show()
        }
    }

    private fun clone(){
        if(bought.id > 0 && boughtCreditCardSvc.clone(bought.id,cache.value)){
            Snackbar.make(
                view,
                R.string.ending_recurrent_copy_payment_successfull,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close) {}
                .show().also { loader.value = false }
        }else{
            Snackbar.make(
                view,
                R.string.dont_copy_payment_successfull,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close, null).show()
        }
    }

    private fun goToEditBought() {
        when(bought.kind) {
            KindInterestRateEnum.CREDIT_CARD -> {
                CreditCardQuotesParams.Companion.ListBought.newInstanceQuote(
                    bought.id,
                    bought.codeCreditCard,
                    navController
                )
            }
            KindInterestRateEnum.CASH_ADVANCE -> {
                CreditCardQuotesParams.Companion.ListBought.newInstanceAdvance(
                    bought.id,
                    bought.codeCreditCard,
                    navController
                )
            }
            KindInterestRateEnum.WALLET_BUY -> {
                CreditCardQuotesParams.Companion.ListBought.newInstanceWallet(
                    bought.id,
                    bought.codeCreditCard,
                    navController
                )
            }
        }
    }

    private fun goToAmortization(){
        val hasDifferInstallment = _differQuotes.firstOrNull{ it.cdBoughtCreditCard.toInt() == bought.id }?.let {true}?:false
        val monthsCalc:Long = if( bought.endDate.toLocalDate() != LocalDate.of(9999,12,31)){
            DateUtils.getMonths(bought.boughtDate,bought.endDate)
        }else{ bought.month.toLong() }
        AmortizationTableParams.newInstanceQuotes(
            CalcMap().mapping(
                bought,
                bought.quoteValue.toBigDecimal(),
                bought.interestValue.toBigDecimal(),
                bought.capitalValue.toBigDecimal(),
                KindOfTaxEnum.valueOf(bought.kindOfTax.getName())
            ), bought.id.toLong()
            ,bought.monthPaid
            ,_creditCard.interest1NotQuote
            ,hasDifferInstallment
            , monthsCalc
            ,navController
        )
    }

    private fun endingDialog(){
            if (boughtCreditCardSvc.endingRecurrentPayment(bought.id, bought.cutOutDate)) {
                Snackbar.make(
                    view,
                    R.string.ending_recurrent_payment_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also { loader.value = false }

            } else {
                Snackbar.make(
                    view,
                    R.string.dont_ending_recurrent_payment,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
    }

    private fun updateRecurrentValueDialog(value:Double){
            if (boughtCreditCardSvc.updateRecurrentValue(bought.id, value,_cutOff,cache.value)) {
                Snackbar.make(
                    view,
                    R.string.ending_recurrent_payment_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also { loader.value = false }
            } else {
                Snackbar.make(
                    view,
                    R.string.dont_ending_recurrent_payment,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
    }

    private fun deleteDialog(){
            if (boughtCreditCardSvc.delete(bought.id,cache.value)) {
                Snackbar.make(
                    view,
                    R.string.delete_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also {
                        loader.value = false
                    }
            } else {
                Snackbar.make(
                    view,
                    R.string.dont_deleted,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
    }

    private fun differntInstallmentDialog(value:Long){
        if(boughtCreditCardSvc.differntInstallment(bought.id, value,_cutOff,cache.value)){
            Snackbar.make(
                view,
                R.string.save_differ_installment,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close) {}
                .show().also { loader.value = false }
        } else {
            Snackbar.make(
                view,
                R.string.not_save_differ_installment,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close, null).show()

        }
    }


    fun getMoreOptionsList():List<MoreOptionsItemsCreditCard>{
        val dateFirst = LocalDate.now().withDayOfMonth(1)
        val dateLast = dateFirst.plusMonths(1).minusDays(1)
        val months = DateUtils.getMonths(bought.boughtDate, LocalDateTime.of(dateLast,LocalTime.MAX))
        var items = MoreOptionsItemsCreditCard.values()
        if(bought.month <= 1){
            items = items.filter { it != MoreOptionsItemsCreditCard.AMORTIZATION}.toTypedArray()
            items = items.filter { it != MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT}.toTypedArray()
        }
        if( (bought.createDate.toLocalDate() !in dateFirst.minusMonths(1)..dateLast)){
            items = items.filter{ it != MoreOptionsItemsCreditCard.EDIT}.toTypedArray()
        }
        if(!bought.recurrent  || (bought.recurrent && bought.createDate.toLocalDate() in dateFirst..dateLast)){
            items = items.filter{ it != MoreOptionsItemsCreditCard.ENDING}.toTypedArray()
            items = items.filter{ it != MoreOptionsItemsCreditCard.UPDATE_VALUE }.toTypedArray()
        }
        if(bought.month > 1 && months < 1 ){
            items = items.filter{ it != MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT }.toTypedArray()
        }

        if(bought.recurrent ){
            items = items.filter{ it != MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT}.toTypedArray()
        }

        if(!Regex("\\(\\d+\\. [\\d\\.]+\\)").containsMatchIn(bought.nameItem)){
            items = items.filter { it != MoreOptionsItemsCreditCard.RESTORE}.toTypedArray()
        }
        return items.toList()
    }
}