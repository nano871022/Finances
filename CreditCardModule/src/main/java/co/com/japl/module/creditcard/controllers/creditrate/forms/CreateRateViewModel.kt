package co.com.japl.module.creditcard.controllers.creditrate.forms

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.module.creditcard.R
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class CreateRateViewModel constructor(private val codeCreditCard:Int?,private val codeCreditRate:Int?,private val creditRateSvc:ITaxPort?,private val creditCardSvc:ICreditCardPort?,private val navCompiler: NavController?): ViewModel() {

    val progress = mutableFloatStateOf(0f)
    val loader = mutableStateOf(true)

    private var _listCreditCards:List<CreditCardDTO> = listOf<CreditCardDTO>()
    private var _creditCardDto:CreditCardDTO? = null
    private var _creditRateDto: TaxDTO? = null
    val creditCards get() = _listCreditCards

    var creditCard = mutableStateOf("")
    var creditCardError = mutableStateOf(false)
    var creditCardKind = mutableStateOf("")
    var creditCardKindError = mutableStateOf(false)
    var month = mutableStateOf("")
    var monthError = mutableStateOf(false)
    var year = mutableStateOf("")
    var yearError = mutableStateOf(false)
    var rate = mutableStateOf("")
    var rateError = mutableStateOf(false)
    var creditRateKind = mutableStateOf("")
    var creditRateKindError = mutableStateOf(false)
    var period = mutableStateOf("")
    var periodError = mutableStateOf(false)
    var status = mutableStateOf(true)
    var periodShow = mutableStateOf(false)

    var save = false

    fun save() {
        validate()
        _creditRateDto?.let {
            creditRateSvc?.let { svc ->
                if (save) {
                    if (it.id == 0) {
                        create(it)
                    }else{
                        update(it)
                    }
                }
            }
        }
    }

        private fun create(rate:TaxDTO){
            if (creditRateSvc?.create(rate)!!) {
                Toast.makeText(
                    navCompiler?.context,
                    R.string.toast_successful_insert,
                    Toast.LENGTH_SHORT
                ).show()
                navCompiler?.navigateUp()
            } else {
                Toast.makeText(
                    navCompiler?.context,
                    R.string.toast_unsuccessful_insert,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun update(rate:TaxDTO){
        if (creditRateSvc?.update(rate)!!) {
            Toast.makeText(
                navCompiler?.context,
                R.string.toast_successful_update,
                Toast.LENGTH_SHORT
            ).show()
            navCompiler?.navigateUp()
        } else {
            Toast.makeText(
                navCompiler?.context,
                R.string.toast_dont_successful_update,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun validate(){
       creditCardError.value = creditCard.value.isEmpty()
       creditCardKindError.value = creditCardKind.value.isEmpty()
       monthError.value = month.value.isEmpty() || !NumbersUtil.isNumber(month.value) || month.value.toShort() <= 0
       yearError.value = year.value.isEmpty() || !NumbersUtil.isNumber(year.value) || year.value.toInt() <= 2020
       rateError.value = rate.value.isEmpty() || !NumbersUtil.isNumber(rate.value) || rate.value.toDouble() <= 0
       creditRateKindError.value = creditRateKind.value.isEmpty()
       periodError.value = period.value.isEmpty() || !NumbersUtil.isNumber(period.value) || period.value.toLong() <= 0
        save = creditCardError.value.not() && creditCardKindError.value.not() && monthError.value.not() && yearError.value.not() && rateError.value.not() && creditRateKindError.value.not()
        if(periodShow.value){
            save = save && periodError.value.not()
        }

       if(save){

           _creditRateDto = TaxDTO(
               codCreditCard = creditCard.value.toInt(),
               kind = KindInterestRateEnum.findByOrdinal(creditCardKind.value?.toShort()?:0),
               month = month.value.toShort(),
               year = year.value.toInt(),
               value = rate.value.toDouble(),
               kindOfTax =  KindOfTaxEnum.findByValue(creditRateKind.value),
               period = if(period.value.isNotEmpty())period.value.toShort() else 0.toShort(),
               status = (if(status.value) 1 else 0).toShort(),
               id = _creditRateDto?.id?:0,
               create = LocalDateTime.now()
           )
       }

    }



    fun main() = runBlocking {
        progress.floatValue = 0.1f
        execute()
        progress.floatValue = 1.0f
    }

    suspend fun execute(){
        codeCreditCard?.let {
            creditCardSvc?.let {
                it.getCreditCard(codeCreditCard)?.let {
                _creditCardDto = it

                    creditCard.value = it.id.toString()

                progress.floatValue = 0.3f
            }
            }
        }
        creditCardSvc?.let {
           _listCreditCards = it.getAll()
            progress.floatValue = 0.6f
            loader.value = false
        }

        year.value = LocalDate.now().year.toString()
        month.value = LocalDate.now().monthValue.toString()
        creditRateKind.value = KindOfTaxEnum.ANUAL_EFFECTIVE.getName()
        creditCardKind.value = KindInterestRateEnum.CREDIT_CARD.getCode().toString()

        creditRateSvc?.let{svc->
            codeCreditCard?.let {
                val yearMonth = YearMonth.of(year.value.toInt(), month.value.toInt()).minusMonths(1)
                svc.get(
                    codeCreditCard,
                    yearMonth.monthValue,
                    yearMonth.year,
                    kind = KindInterestRateEnum.findByOrdinal(creditCardKind.value?.toShort()?:0))?.let {
                        rate.value = it.value.toString()
                    }
            }
            codeCreditRate?.let {
                svc.getById(it)?.let {
                    _creditRateDto = it
                    year.value = it.year.toString()
                    month.value = it.month.toString()
                    rate.value = it.value.toString()
                    creditCardKind.value = it.kind.getCode().toString()
                    creditRateKind.value = it.kindOfTax?.getName()?:KindOfTaxEnum.ANUAL_EFFECTIVE.getName()
                    period.value = it.period.toString()
                    status.value = it.status>0
                }
            }
        }

    }

}