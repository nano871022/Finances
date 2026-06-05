package co.com.japl.module.credit.controllers.forms

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.AdditionalCreditDTO
import co.com.japl.finances.iports.outbounds.IAdditionalPort
import co.com.japl.module.credit.R
import co.com.japl.ui.utils.initialFieldState
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.module.credit.params.AdditionalCreditParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AdditionalFormViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val additionalSvc: IAdditionalPort
) : ViewModel(){

    private val params = AdditionalCreditParams.download(savedStateHandle)
    private val id: Int = params.first
    private val codeCredit: Int = params.second
    var navController: NavController? = null


    private val _dto = MutableStateFlow<AdditionalCreditDTO>(AdditionalCreditDTO(
        id=id,
        creditCode = codeCredit.toLong(),
        name = "",
        value = BigDecimal.ZERO,
        startDate = LocalDate.now(),
        endDate = LocalDate.now()
    ))
    val hostState: SnackbarHostState = SnackbarHostState()
    val loading = mutableStateOf(true)
    val name = initialFieldState(
        savedStateHandle,
        "FORM_NAME",
        initialValue = "",
        validator = { it.isNotBlank()},
        onValueChangeCallBack = {name->_dto.update{
            it.copy(name = name)
        }}
    )
    val value = initialFieldState<BigDecimal>(
        savedStateHandle,
        "FORM_VALUE",
        initialValue = BigDecimal.ZERO,
        formatter = { if(it.isNotBlank() && NumbersUtil.isNumber(it)) NumbersUtil.toBigDecimal(it) else BigDecimal.ZERO  },
        validator = { BigDecimal.ZERO < it },
        onValueChangeCallBack = { value ->
            _dto.update {
                it.copy(value = value)
            }
        }
    ).also {
        it.onValueChangeStr("")
    }
    val startDate = initialFieldState(
        savedStateHandle,
        "FORM_START_DATE",
        initialValue = LocalDate.now(),
        validator = { it.isAfter(LocalDate.now().withYear(2000))},
        onValueChangeCallBack = { date->
            _dto.update {
                it.copy(startDate = date)
            }
        }
    )

    fun create() {
        if (validation()) {
            viewModelScope.launch {
                additionalSvc?.let { svcPort ->
                    try {
                        if (_dto.value.id <= 0) {
                            val result = withContext(Dispatchers.IO) {
                                svcPort.create(_dto.value)
                            }
                            if (result) {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.create_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                ).also {
                                    navController?.popBackStack()
                                }
                            } else {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.error_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            val result = withContext(Dispatchers.IO) {
                                svcPort.update(_dto.value)
                            }
                            if (result) {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.update_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                ).also {
                                    navController?.popBackStack()
                                }
                            } else {
                                hostState.showSnackbar(
                                    message = context.getString(R.string.error_upd_record_success),
                                    actionLabel = context.getString(R.string.close),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } catch (e: Exception) {
                        hostState.showSnackbar(
                            message = "Error: ${e.message}",
                            actionLabel = context.getString(R.string.close),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    fun clear(){
        name.reset("")
        value.reset(BigDecimal.ZERO)
        startDate.reset(LocalDate.now())
    }

    fun validation():Boolean {
        name.validate().takeIf { !it }?.let {
            name.error.value = false
        }
        value.validate().takeIf { !it }?.let {
            value.error.value = false
        }
        startDate.validate().takeIf { !it }?.let {
            startDate.error.value = false
        }
        return name.error.value.not() && value.error.value.not() && startDate.error.value.not()
    }

    fun execute()=viewModelScope.launch{
        additionalSvc?.let{ svcPort ->
            Log.d(this.javaClass.name,"<<<=== Execute:List $id $codeCredit")
            id.takeIf { it > 0 }?.let {
                withContext(Dispatchers.IO) {
                    svcPort.get(id)
                }?.let{
                    Log.d(this.javaClass.name,"<<<=== Execute:List $id $codeCredit $it")
                    name.onValueChange(it.name)
                    value.onValueChange(it.value)
                    startDate.onValueChange(it.startDate)
                    _dto.update{
                        it.copy(
                            name = it.name,
                            value = it.value,
                            startDate = it.startDate,
                            endDate = it.endDate
                        )
                    }
                }
            }
        }
        loading.value = false
    }
}
