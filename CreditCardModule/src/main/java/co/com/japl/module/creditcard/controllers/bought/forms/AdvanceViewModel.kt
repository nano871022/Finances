package co.com.japl.module.creditcard.controllers.bought.forms

import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.module.creditcard.R
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime

class AdvanceViewModel constructor(private val codeCreditCard:Int, private val codeBought:Int, private val period:LocalDateTime, private val boughtSvc:IBoughtPort?, private val creditRateSvc:ITaxPort?, private val creditCardSvc:ICreditCardPort?, private val navController: NavController?, private val prefs:Prefs) : ViewModel(){

    private var cutOffDate:LocalDateTime? = null
    private var bought:CreditCardBoughtDTO? = null
    private var validate = false
    private var taxDto:TaxDTO? = null

    val loading = mutableStateOf(true)
    val progress = mutableFloatStateOf(0.0f)

    val isNew = mutableStateOf(true)

    val creditCardName = mutableStateOf("")
    val nameProduct = mutableStateOf("")
    val errorNameProduct = mutableStateOf(false)
    val valueProduct = mutableStateOf("")
    val errorValueProduct = mutableStateOf(false)
    val monthProduct = mutableStateOf("")
    val creditRate = mutableStateOf("")
    val capitalValue = mutableStateOf("")
    val dateBought = mutableStateOf("")
    val errorDateBought = mutableStateOf(false)
    val quoteValue = mutableStateOf("")
    val interestValue = mutableStateOf("")
    val creditRateKind = mutableStateOf("")

    fun clear(){

        nameProduct.value = ""
        valueProduct.value = ""
        monthProduct.value = ""
        creditRate.value = ""
        capitalValue.value = ""
        isNew.value = true
        quoteValue.value = ""
        interestValue.value = ""

        errorValueProduct.value = false
        errorNameProduct.value = false
        errorDateBought.value = false

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

    fun validate(){
        var value = true
        var month = true

        validate = true
        nameProduct.value.takeIf { it.isBlank() }?.let {
            errorNameProduct.value = true
            validate = false
        }?:errorNameProduct.takeIf { it.value }?.let{ it.value = false}

        valueProduct.value.takeIf { it.isBlank() && NumbersUtil.isNumber(it).not() }?.let {
            errorValueProduct.value = true
            validate = false
            value = false
        }?:errorValueProduct.takeIf { it.value }?.let { it.value = false}

        dateBought.value.takeIf { it.isBlank() && DateUtils.isDateValid(it).not() }?.let {
            errorDateBought.value = true
            validate = false
        }?:errorDateBought.takeIf { it.value }?.let { it.value = false}

        if(month && value){
            val month = NumbersUtil.toLong(monthProduct.value).toInt()
            val value = NumbersUtil.toBigDecimal(valueProduct.value)
            boughtSvc?.let {
                taxDto?.let {taxDto->
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
                    quoteValue.value = NumbersUtil.COPtoString(quoteBought)
                    interestValue.value = NumbersUtil.COPtoString(interestBought)
                    capitalValue.value = NumbersUtil.COPtoString(capitalBought)
                }
            }

        }

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
                kind = KindInterestRateEnum.CASH_ADVANCE,
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
        dateBought.value = DateUtils.localDateToStringDate(LocalDate.now())
        progress.floatValue = 0.3f
        creditCardSvc?.let {
            it.getCreditCard(codeCreditCard)?.let {
                creditCardName.value = it.name
                DateUtils.cutOff(it.cutOffDay, period.toLocalDate())?.let {
                    cutOffDate = it
                }
                progress.floatValue = 0.5f
            }
        }

        creditRateSvc?.let {
            it.get(codeCreditCard,cutOffDate?.monthValue?:period.monthValue,cutOffDate?.year?:period.year,KindInterestRateEnum.CASH_ADVANCE)?.let {
                taxDto = it
                creditRate.value = it.value.toString()
                creditRateKind.value = it.kindOfTax?.getName()?:"EM"
                monthProduct.value = it.period.toString()
                navController?.let {
                    nameProduct.value = it.context.getString(R.string.CASH_ADVANCE)
                }
                progress.floatValue = 0.8f
            }
        }

        boughtSvc?.let {
            if(codeBought > 0) {
                it.getById(codeBought, prefs.simulator)?.let {
                    bought = it

                    dateBought.value = DateUtils.localDateTimeToStringDate(it.boughtDate)
                    nameProduct.value = it.nameItem
                    valueProduct.value = NumbersUtil.toString(it.valueItem)
                    monthProduct.value = it.month.toString()
                    validate()


                    isNew.value = false
                }
            }
        }
    }


}