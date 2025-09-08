package co.com.japl.module.creditcard.controllers.bought.forms

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
import co.japl.android.graphs.utils.NumbersUtil
import co.com.japl.ui.utils.initialFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val savedStateHolder: SavedStateHandle,
    private val boughtSvc:IBoughtPort,
    private val creditRateSvc:ITaxPort,
    private val creditCardSvc:ICreditCardPort,
    private val prefs:Prefs,
    @ApplicationContext private val context:Context
) : ViewModel(){

    private var cutOffDate:LocalDateTime? = null
    private var bought:CreditCardBoughtDTO? = null
    private var validate = false
    private var taxDto:TaxDTO? = null

    private var codeCreditCard:Int = 0
    private var codeBought:Int = 0
    private var period:LocalDateTime = LocalDateTime.now()


    val loading = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val isNew = mutableStateOf(true)

    val creditCardName = initialFieldState(
        savedStateHolder,
        "FORM_CREDIT_CARD_NAME",
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { bought?.nameCreditCard = it }
    )

    val nameProduct = initialFieldState(
        savedStateHolder,
        "FORM_NAME_PRODUCT",
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { bought?.nameItem = it; validate() }
    )

    val valueProduct = initialFieldState(
        savedStateHolder,
        "FORM_VALUE_PRODUCT",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { bought?.valueItem = NumbersUtil.toBigDecimal(it); validate() }
    )
    val monthProduct = initialFieldState(
        savedStateHolder,
        "FORM_MONTH_PRODUCT",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it) && it.toInt() > 0},
        onValueChangeCallBack = { bought?.month = it.toInt(); validate() }
    )
    val creditRate = initialFieldState(
        savedStateHolder,
        "FORM_CREDIT_RATE",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { bought?.interest = NumbersUtil.toBigDecimal(it).toDouble() }
    )
    val capitalValue = initialFieldState(
        savedStateHolder,
        "FORM_CAPITAL_VALUE",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )
    val dateBought = initialFieldState(
        savedStateHolder,
        "FORM_DATE_BOUGHT",
        initialValue = "",
        validator = {it.isNotBlank() && DateUtils.isDateValid(it)},
        onValueChangeCallBack = { bought?.boughtDate = DateUtils.toLocalDateTime2(it) }
    )
    val quoteValue = initialFieldState(
        savedStateHolder,
        "FORM_QUOTE_VALUE",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )

    val interestValue = initialFieldState(
        savedStateHolder,
        "FORM_INTEREST_VALUE",
        initialValue = "",
        validator = {it.isNotBlank() && NumbersUtil.isNumber(it)},
        onValueChangeCallBack = { }
    )
    val creditRateKind = initialFieldState(
        savedStateHolder,
        "FORM_CREDIT_RATE_KIND",
        initialValue = "",
        validator = {it.isNotBlank()},
        onValueChangeCallBack = { }
    )

    init{
        savedStateHolder.get<Int>("codeCreditCard")?.let{ codeCreditCard = it }
        savedStateHolder.get<Int>("codeBought")?.let{ codeBought = it }

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

    fun create(navController: NavController){
        if(validate) {
            boughtSvc.let {
                if(it.create(bought!!,prefs.simulator)>0) {
                    Toast.makeText(
                        navController.context,
                        R.string.toast_successful_insert,
                        Toast.LENGTH_SHORT
                    ).show().also {
                        navController.popBackStack()
                    }
                }else {
                    Toast.makeText(
                        navController.context,
                        R.string.toast_unsuccessful_insert,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun update(navController: NavController){
        if(validate) {
            boughtSvc.let {
                if(it.update(bought!!,prefs.simulator)) {
                    Toast.makeText(
                        navController.context,
                        R.string.toast_successful_update,
                        Toast.LENGTH_SHORT
                    ).show().also {
                        navController.popBackStack()
                    }
                }else {
                    Toast.makeText(
                        navController.context,
                        R.string.toast_dont_successful_update,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun creditRateEmpty(navController: NavController){
        if(taxDto == null){
            Toast.makeText(navController.context,R.string.toast_credit_rate_is_not_setting,Toast.LENGTH_SHORT).show().also {
                navController.popBackStack()
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
            val month = NumbersUtil.toLong(monthProduct.value.value).toInt()
            val value = NumbersUtil.toBigDecimal(valueProduct.value.value)
            boughtSvc.let {
                taxDto?.let { taxDto ->
                    val quoteBought = it.quoteValue(
                        taxDto.id,
                        month.toShort(),
                        value.toDouble(),
                        taxDto.kindOfTax!!,
                        taxDto.kind
                    )
                    val interestBought = it.interestValue(
                        taxDto.id,
                        month.toShort(),
                        value.toDouble(),
                        taxDto.kindOfTax!!,
                        taxDto.kind
                    )
                    val capitalBought = it.capitalValue(
                        taxDto.id,
                        month.toShort(),
                        value.toDouble(),
                        taxDto.kindOfTax!!,
                        taxDto.kind
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
                nameItem = nameProduct.value.value,
                valueItem = NumbersUtil.toBigDecimal(valueProduct.value.value),
                month = NumbersUtil.toLong(monthProduct.value.value).toInt(),
                boughtDate = DateUtils.toLocalDateTime2(dateBought.value.value),
                createDate = LocalDateTime.now(),
                endDate = LocalDateTime.MAX,
                cutOutDate = period,
                interest = NumbersUtil.toBigDecimal(creditRate.value.value).toDouble(),
                kind = KindInterestRateEnum.WALLET_BUY,
                kindOfTax = taxDto?.kindOfTax!!,
                nameCreditCard = creditCardName.value.value,
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
        creditCardSvc.let {
            it.getCreditCard(codeCreditCard)?.let {
                creditCardName.onValueChange(it.name)
                DateUtils.cutOff(it.cutOffDay, period.toLocalDate())?.let {
                    cutOffDate = it
                }
                progress.floatValue = 0.5f
            }
        }

        creditRateSvc.let {
            it.get(codeCreditCard,cutOffDate?.monthValue!!,cutOffDate?.year!!,KindInterestRateEnum.WALLET_BUY)?.let {
                taxDto = it
                creditRate.onValueChange(it.value.toString())
                creditRateKind.onValueChange(it.kindOfTax?.getName()?:"")
                nameProduct.onValueChange(context.getString(R.string.WALLET_BUY))
                progress.floatValue = 0.8f
            }
        }

        boughtSvc.let {
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