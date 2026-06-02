package co.com.japl.module.creditcard.controllers.account

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.navigations.ListCreditCardSettings
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.module.creditcard.params.CreditCardParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.LocalDateTime
import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject

@HiltViewModel
class CreditCardViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val creditCardSvc:ICreditCardPort
):ViewModel() {

    private val codeCreditCard: Int? = CreditCardParams.download(savedStateHandle).orElse(null)?.toIntOrNull()
    var navController: NavController? = null

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

        hasErrorName.value = name.value.trim().isEmpty()
        hasErrorQuoteMax.value = maxQuotes.value.trim().isEmpty() || NumbersUtil.isNumber(maxQuotes.value.trim()).not() || (maxQuotes.value.trim().toIntOrNull() ?: 0) <= 0
        hasErrorCutOfDay.value = cutOffDay.value.trim().isEmpty() || NumbersUtil.isNumber(cutOffDay.value.trim()).not() || ((cutOffDay.value.trim().toIntOrNull() ?: 0) <= 0 || (cutOffDay.value.trim().toIntOrNull() ?: 0) > 31)
        hasErrorWarning.value = warningValue.value.trim().isEmpty() || NumbersUtil.isNumber(warningValue.value.trim()).not() || (NumbersUtil.toBigDecimal(warningValue.value.trim()) <= BigDecimal.ZERO)
        showButtons.value = (hasErrorName.value || hasErrorQuoteMax.value || hasErrorCutOfDay.value || hasErrorWarning.value).not()

        if(showButtons.value) {
            _creditCardDto?.let {
                it.name = name.value.trim()
                it.maxQuotes = maxQuotes.value.trim().toShortOrNull() ?: 0
                it.cutOffDay = cutOffDay.value.trim().toShortOrNull() ?: 0
                it.warningValue = NumbersUtil.toBigDecimal(warningValue.value.trim())
                it.status = state.value
                it.interest1Quote = interest1Quote.value
                it.interest1NotQuote = interest1NotQuote.value
            }
        }
    }

    fun create(){
        if(showButtons.value) {
            _creditCardDto?.let {
                val id = creditCardSvc.create(it)
                if (id > 0) {
                    navController?.let { nav ->
                        Toast.makeText(
                            nav.context,
                            R.string.toast_successful_insert,
                            Toast.LENGTH_LONG
                        ).show()
                        nav.navigateUp()
                    }
                } else {
                    navController?.let { nav ->
                        Toast.makeText(
                            nav.context,
                            R.string.toast_unsuccessful_insert,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }else{
            validate()
        }
    }

    fun update(){
        _creditCardDto?.let{
            if(creditCardSvc.update(_creditCardDto!!)){
                navController?.let { nav ->
                    Toast.makeText(nav.context, R.string.toast_successful_update,Toast.LENGTH_LONG).show()
                    nav.navigateUp()
                }
            }else{
                navController?.let { nav ->
                    Toast.makeText(nav.context, R.string.toast_dont_successful_update,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun goSettings(){
        navController?.let { nav ->
            codeCreditCard?.let {id->
                ListCreditCardSettings.navigate(codeCreditCard, nav)
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
                warningValue.value = if(it.warningValue > BigDecimal.ZERO) {
                    NumbersUtil.toString(it.warningValue.toDouble())
                }else{
                    ""
                }
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
