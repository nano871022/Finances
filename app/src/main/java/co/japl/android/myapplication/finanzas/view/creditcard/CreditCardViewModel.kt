package co.japl.android.myapplication.finanzas.view.creditcard

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.ListCreditCardSettingParams
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.LocalDateTime

class CreditCardViewModel constructor(private val codeCreditCard:Int?,private val creditCardSvc:ICreditCardPort, private val navController: NavController):ViewModel() {
    var showProgress = mutableStateOf(true)
    var progress = mutableFloatStateOf(0f)
    var showButtonUpdate = mutableStateOf(false)
    var showButtons = mutableStateOf(false)

    var name = mutableStateOf("")
    var maxQuotes = mutableStateOf("")
    var cutOffDay = mutableStateOf("")
    var warningValue = mutableStateOf("")
    var state = mutableStateOf(true)
    var interest1Quote = mutableStateOf(false)
    var interest1NotQuote = mutableStateOf(false)

    var hasErrorName = mutableStateOf(false)
    var hasErrorQuoteMax = mutableStateOf(false)
    var hasErrorCutOfDay = mutableStateOf(false)
    var hasErrorWarning = mutableStateOf(false)

    private var _creditCardDto:CreditCardDTO? = CreditCardDTO(id=0,name="", maxQuotes = 0, cutOffDay = 1, warningValue = BigDecimal.ZERO, create = LocalDateTime.now(), status = true, interest1Quote = false, interest1NotQuote = false)

    fun validate(){

        hasErrorName.value = name.value.isEmpty()
        hasErrorQuoteMax.value = maxQuotes.value.isEmpty() || maxQuotes.value.toInt() <= 0
        hasErrorCutOfDay.value = cutOffDay.value.isEmpty() || (cutOffDay.value.toInt() <= 0 && cutOffDay.value.toInt() > 30)
        hasErrorWarning.value = warningValue.value.isEmpty() || NumbersUtil.toBigDecimal(warningValue.value) <= BigDecimal.ZERO
        if(!hasErrorWarning.value){
            warningValue.value = NumbersUtil.toString(NumbersUtil.toBigDecimal(warningValue.value))
        }

        showButtons.value = !(hasErrorName.value || hasErrorQuoteMax.value || hasErrorCutOfDay.value || hasErrorWarning.value)

        if(showButtons.value) {
            _creditCardDto?.let {
                it.name = name.value
                it.maxQuotes = maxQuotes.value.toShort()
                it.cutOffDay = cutOffDay.value.toShort()
                it.warningValue = NumbersUtil.toBigDecimal(warningValue.value)
                it.status = state.value
                it.interest1Quote = interest1Quote.value
                it.interest1NotQuote = interest1NotQuote.value
            }
        }
    }

    fun create(){
        _creditCardDto?.let{
            val id = creditCardSvc.create(it)
            if(id > 0){
                Toast.makeText(navController.context, R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
                navController.navigateUp()
            }else{
                Toast.makeText(navController.context, R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun update(){
        _creditCardDto?.let{
            if(creditCardSvc.update(_creditCardDto!!)){
                Toast.makeText(navController.context, R.string.toast_successful_update,Toast.LENGTH_LONG).show()
                navController.navigateUp()
            }else{
                Toast.makeText(navController.context, R.string.toast_dont_successful_update,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun goSettings(){
        navController?.let {
            codeCreditCard?.let {id->
                ListCreditCardSettingParams.newInstance(id, it)
            }
        }
    }

    fun main() = runBlocking {
        progress.floatValue = 0.1f

        execute()

        progress.floatValue = 1f
    }

    suspend fun execute(){
        progress.floatValue = 0.3f
        codeCreditCard?.let {
            creditCardSvc.getCreditCard(codeCreditCard)?.let{
                _creditCardDto = it
                showButtonUpdate.value = it.id > 0

                name.value = it.name
                maxQuotes.value = it.maxQuotes.toString()
                cutOffDay.value = it.cutOffDay.toString()
                warningValue.value = it.warningValue.toString()
                state.value = it.status
                interest1Quote.value = it.interest1Quote
                interest1NotQuote.value = it.interest1NotQuote

                validate()
            }
        }
        showProgress.value = false
        progress.floatValue = 0.8f
    }
}