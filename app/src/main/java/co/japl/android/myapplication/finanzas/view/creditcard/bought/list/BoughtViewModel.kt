package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
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
    private val application:Application
    ):AndroidViewModel(application) {

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
        }
    }

    private fun goToEditBought() {
        CreditCardQuotesParams.Companion.ListBought.newInstance(
            bought.id,
            bought.codeCreditCard,
            navController
        )
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
                    .show().also { navController.popBackStack() }

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
            if (boughtCreditCardSvc.updateRecurrentValue(bought.id, value,_cutOff)) {
                Snackbar.make(
                    view,
                    R.string.ending_recurrent_payment_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also { navController.popBackStack() }
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
            if (boughtCreditCardSvc.delete(bought.id)) {
                Snackbar.make(
                    view,
                    R.string.delete_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also { navController.popBackStack() }
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
        if(boughtCreditCardSvc.differntInstallment(bought.id, value,_cutOff)){
            Snackbar.make(
                view,
                R.string.save_differ_installment,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.close) {}
                .show().also { navController.popBackStack() }
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
        if(bought.createDate.toLocalDate() !in dateFirst..dateLast){
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
        return items.toList()
    }
}