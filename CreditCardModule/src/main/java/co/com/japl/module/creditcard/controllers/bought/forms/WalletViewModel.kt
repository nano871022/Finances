package co.com.japl.module.creditcard.controllers.bought.forms

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.initialFieldState
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime

class WalletViewModel constructor(private val codeCreditCard:Int,private val codeBought:Int,private val period:LocalDateTime,private val boughtSvc:IBoughtPort?,private val creditRateSvc:ITaxPort?,private val creditCardSvc:ICreditCardPort?,private val navController: NavController?,private val prefs:Prefs) : ViewModel(){

    private var cutOffDate:LocalDateTime? = null
    private var bought:CreditCardBoughtDTO? = null
    private var validate = false
    private var taxDto:TaxDTO? = null

    val loading = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val isNew = mutableStateOf(true)

    val creditCardName = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { bought?.nameCreditCard = it }
    )

    val nameProduct = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { bought?.nameItem = it; validate() }
    )

    val valueProduct = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { bought?.valueItem = NumbersUtil.toBigDecimal(it); validate() }
    )
    val monthProduct = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it) && it.toInt() > 0},
        onValueChangeCallBack = { bought?.month = it.toInt(); validate() }
    )
    val creditRate = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { bought?.interest = NumbersUtil.toBigDecimal(it).toDouble() }
    )
    val capitalValue = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )
    val dateBought = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && DateUtils.isDateValid(it)},
        onValueChangeCallBack = { bought?.boughtDate = DateUtils.toLocalDateTime2(it) }
    )
    val quoteValue = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )

    val interestValue = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )
    val creditRateKind = initialFieldState(
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { }
    )

    init{
        main()
    }

    fun clear(){

        nameProduct.reset("")
        valueProduct.reset("")
        monthProduct.reset("")
        creditRate.reset("")
        capitalValue.reset("")
        isNew.value = true
        quoteValue.reset("")
        interestValue.reset("")

    }

    fun create(){
        if(validate) {
            boughtSvc?.let {
                if(it.create(bought!!,prefs.simulator)>0) {
                    navController?.let { navController ->
                        Toast.makeText(
                            navController.context,
                            R.string.toast_successful_insert,
                            Toast.LENGTH_SHORT
                        ).show().also {
                            navController.popBackStack()
                        }
                    }
                }else {
                    Toast.makeText(
                        navController?.context,
                        R.string.toast_unsuccessful_insert,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun update(){
        if(validate) {
            boughtSvc?.let {
                if(it.update(bought!!,prefs.simulator)) {
                    navController?.let { navController ->
                        Toast.makeText(
                            navController.context,
                            R.string.toast_successful_update,
                            Toast.LENGTH_SHORT
                        ).show().also {
                            navController.popBackStack()
                        }
                    }
                }else {
                    Toast.makeText(
                        navController?.context,
                        R.string.toast_dont_successful_update,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun creditRateEmpty(){
        if(taxDto == null){
            navController?.let {navController->
                Toast.makeText(navController.context,R.string.toast_credit_rate_is_not_setting,Toast.LENGTH_SHORT).show().also {
                    navController.popBackStack()
                }
            }
        }
    }

    fun validate() {
        var value = true
        var month = true

        validate = true
        nameProduct.validate().not().or(nameProduct.error.value).takeIf { it }
            ?.let { validate = false }
        valueProduct.validate().not().or(valueProduct.error.value).takeIf { it }
            ?.let { validate = false; value = false }

        monthProduct.validate().not().or(monthProduct.error.value).takeIf { it }?.let {
            validate = false;month = false
        }

        dateBought.validate().not().or(dateBought.error.value).takeIf { it }
            ?.let { validate = false }

        calculateValues(month,value)
        createNewDto()
    }

    private fun calculateValues(month:Boolean,value: Boolean) {
        if (month && value) {
            val month = NumbersUtil.toLong(monthProduct.value).toInt()
            val value = NumbersUtil.toBigDecimal(valueProduct.value)
            boughtSvc?.let {
                taxDto?.let { taxDto ->
                    val quoteBought = it.quoteValue(
                        taxDto?.id!!,
                        month.toShort(),
                        value.toDouble(),
                        taxDto?.kindOfTax!!,
                        taxDto?.kind!!
                    )
                    val interestBought = it.interestValue(
                        taxDto?.id!!,
                        month.toShort(),
                        value.toDouble(),
                        taxDto?.kindOfTax!!,
                        taxDto?.kind!!
                    )
                    val capitalBought = it.capitalValue(
                        taxDto?.id!!,
                        month.toShort(),
                        value.toDouble(),
                        taxDto?.kindOfTax!!,
                        taxDto?.kind!!
                    )
                    quoteValue.onValueChange(NumbersUtil.COPtoString(quoteBought))
                    interestValue.onValueChange(NumbersUtil.COPtoString(interestBought))
                    capitalValue.onValueChange(NumbersUtil.COPtoString(capitalBought))
                }
            }

        }
    }

    private fun createNewDto(){
        if(validate && taxDto != null){
            bought = CreditCardBoughtDTO(
                codeCreditCard = codeCreditCard,
                id = codeBought,
                nameItem = nameProduct.value,
                valueItem = NumbersUtil.toBigDecimal(valueProduct.value),
                month = NumbersUtil.toLong(monthProduct.value).toInt(),
                boughtDate = DateUtils.toLocalDateTime2(dateBought.value),
                createDate = LocalDateTime.now(),
                endDate = LocalDateTime.MAX,
                cutOutDate = period,
                interest = NumbersUtil.toBigDecimal(creditRate.value).toDouble(),
                kind = KindInterestRateEnum.WALLET_BUY,
                kindOfTax = taxDto?.kindOfTax!!,
                nameCreditCard = creditCardName.value,
                recurrent = 0
            )
        }
    }

    fun main()= runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
        loading.value = false
    }

    suspend fun execute(){
        progress.floatValue = 0.2f
        dateBought.onValueChange(DateUtils.localDateToStringDate(LocalDate.now()))
        progress.floatValue = 0.3f
        creditCardSvc?.let {
            it.getCreditCard(codeCreditCard)?.let {
                creditCardName.onValueChange(it.name)
                DateUtils.cutOff(it.cutOffDay, period.toLocalDate())?.let {
                    cutOffDate = it
                }
                progress.floatValue = 0.5f
            }
        }

        creditRateSvc?.let {
            it.get(codeCreditCard,cutOffDate?.monthValue!!,cutOffDate?.year!!,KindInterestRateEnum.WALLET_BUY)?.let {
                taxDto = it
                creditRate.onValueChange(it.value.toString())
                creditRateKind.onValueChange(it.kindOfTax?.getName()?: KindOfTaxEnum.MONTHLY_EFFECTIVE.value)
                navController?.let {
                    nameProduct.onValueChange(it.context.getString(R.string.WALLET_BUY))
                }
                progress.floatValue = 0.8f
            }
        }

        boughtSvc?.let {
            if(codeBought > 0) {
                it.getById(codeBought, prefs.simulator)?.let {
                    bought = it

                    dateBought.onValueChange(DateUtils.localDateTimeToStringDate(it.boughtDate))
                    nameProduct.onValueChange(it.nameItem)
                    valueProduct.onValueChange(NumbersUtil.toString(it.valueItem))
                    monthProduct.onValueChange(it.month.toString())
                    validate()

                    isNew.value = false
                }
            }
        }
    }


}