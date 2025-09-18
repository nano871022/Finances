package co.com.japl.module.paid.controllers.Inputs.form

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.enums.MoreOptionsKindPaymentInput
import co.com.japl.ui.utils.DateUtils
import co.japl.android.graphs.utils.NumbersUtil
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import androidx.lifecycle.SavedStateHandleSupport

class InputViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val inputSvc: IInputPort
) : ViewModel() {
    private var codeAccount: Int? = null
    private var codeInput: Int? = null

    var loader = mutableStateOf(true)
    var progress = mutableFloatStateOf(0f)
    private var _input: InputDTO? = null

    val date = mutableStateOf("")
    val kindOfPayment = mutableStateOf("")
    val name = mutableStateOf("")
    val value = mutableStateOf("")

    val errorDate = mutableStateOf(false)
    val errorKindOfPayment = mutableStateOf(false)
    val errorName = mutableStateOf(false)
    val errorValue = mutableStateOf(false)

    private var save = false

    init {
        codeAccount = savedStateHandle.get<Int>("account_code")
        codeInput = savedStateHandle.get<Int>("input_code")
        main()
    }

    fun save(navController: NavController, context: Context) {
        if (save) {
            _input?.let {
                if (it.id == 0) {
                    if (inputSvc.create(it)) {
                        navController.navigateUp()
                        Toast.makeText(context, R.string.toast_save_successful, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.toast_save_error, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (inputSvc.update(it)) {
                        navController.navigateUp()
                        Toast.makeText(context, R.string.toast_update_successful, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.toast_update_error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            validation()
        }
    }

    fun validation() {
        errorDate.value = date.value.isEmpty()
        errorKindOfPayment.value = kindOfPayment.value.isEmpty()
        errorName.value = name.value.isEmpty()
        errorValue.value = value.value.isEmpty() && !NumbersUtil.isNumber(value.value)

        save = !errorDate.value && !errorKindOfPayment.value && !errorName.value && !errorValue.value

        if (save) {
            val input = InputDTO(
                _input?.id ?: 0,
                DateUtils.toLocalDate(date.value),
                codeAccount!!,
                kindOfPayment.value,
                name.value,
                value.value.toBigDecimal(),
                LocalDate.now(),
                LocalDate.MAX
            )
            _input = input
        }
    }

    private fun main() = runBlocking {
        progress.value = 0.1f
        execution()
        progress.value = 1f
    }

    private suspend fun execution() {
        date.value = DateUtils.localDateToStringDate(LocalDate.now())
        kindOfPayment.value = MoreOptionsKindPaymentInput.MONTHLY.getName()
        codeInput?.let {
            inputSvc.getById(it)?.let {
                _input = it
                date.value = DateUtils.localDateToStringDate(it.date)
                kindOfPayment.value = it.kindOf
                name.value = it.name
                value.value = it.value.toString()
                loader.value = false
            }
        }
        loader.value = false
    }

    companion object {
        fun create(extras: CreationExtras, inputSvc: IInputPort): InputViewModel {
            val savedStateHandle = SavedStateHandleSupport.createSavedStateHandle(extras)
            return InputViewModel(savedStateHandle, inputSvc)
        }
    }
}